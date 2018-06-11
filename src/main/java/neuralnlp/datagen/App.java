package neuralnlp.datagen;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import edu.knowitall.openie.Instance;
import edu.knowitall.openie.OpenIE;

/**
 * Hello world!
 *
 */
import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.naturalli.NaturalLogicAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.simple.*;
import edu.stanford.nlp.util.CoreMap;
import scala.collection.JavaConversions;
import edu.knowitall.tool.parse.ClearParser;
import edu.knowitall.tool.postag.ClearPostagger;
import edu.knowitall.tool.srl.ClearSrl;
import edu.knowitall.tool.tokenize.ClearTokenizer;
import scala.collection.Seq;
import edu.knowitall.openie.Argument;

/**
 * A demo illustrating how to call the OpenIE system programmatically.
 */
public class App {

	static java.util.List<Instance> convert(scala.collection.Seq<Instance> seq) {
		return scala.collection.JavaConversions.seqAsJavaList(seq);
	}

	public static void main(String[] args) throws Exception {
		// File inFile = new File("../../data/sentences.txt");

		Properties props = new Properties();
		props.setProperty("annotators", "tokenize,ssplit,pos,lemma,depparse,natlog,openie");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

		Annotation doc = new Annotation("Tesla prejudiced against overweight people."
				+ " He did fire a secretary."
				+ " Tesla influenced by while in school Martin Johnson."
				+ " German classes were held in Tesla's school.".replaceAll("\n|\t", " "));

		OpenIE openie = new OpenIE(new ClearParser(new ClearPostagger(new ClearTokenizer())), new ClearSrl(), false,
				false);
		pipeline.annotate(doc);
		boolean append = true;
		if ((args.length > 0) && (args[0].equals("overwrite")))
			append = false;
		File uw = new File("./../../data/sentencesUW.txt");
		PrintWriter uwFw = new PrintWriter(new FileOutputStream(uw, append));
		File stanford = new File("./../../data/sentencesStanford.txt");
		PrintWriter stanfordFw = new PrintWriter(new FileOutputStream(stanford, append));
		long time = System.currentTimeMillis();

		for (CoreMap sentence : doc.get(CoreAnnotations.SentencesAnnotation.class)) { // Iterate over the sentences in
																						// the document
			Collection<RelationTriple> triples = sentence.get(NaturalLogicAnnotations.RelationTriplesAnnotation.class);
			if (!triples.isEmpty())
				stanfordFw.println(sentence.toString());
			for (RelationTriple triple : triples) {
				// Print the triple
				if (triple.confidence > 0.99)
					stanfordFw.println(
							triple.subjectGloss() + "\t" + triple.relationGloss() + "\t" + triple.objectGloss());
			}
			if (!triples.isEmpty())
				stanfordFw.println();
			Seq<Instance> extractions = openie.extract(sentence.toString());
			List<Instance> list_extractions = JavaConversions.seqAsJavaList(extractions);
			if (!triples.isEmpty())
				uwFw.println(sentence.toString());
			for (Instance instance : list_extractions) {
				List<Argument> list_arg2s = JavaConversions.seqAsJavaList(instance.extr().arg2s());
				for (Argument argument : list_arg2s) {
					if (instance.confidence() > 0.7) {
						uwFw.print(instance.extr().arg1().displayText() + "\t");
						uwFw.print(instance.extr().rel().text() + "\t");
						uwFw.print(argument.text() + "\t");
						uwFw.println(instance.confidence() + "\t");
					}
				}
			}
			if (!triples.isEmpty())
				uwFw.println();
		}
		stanfordFw.close();
		uwFw.close();
		System.out.println((System.currentTimeMillis() - time) / 1000);
	}
}