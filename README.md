# Legacy-Open-IE

This is a Java maven project showing use of [OpenIE-4](https://github.com/allenai/openie-standalone) and Stanford [OpenIE](https://nlp.stanford.edu/software/openie.html). Please see their project page and licenses (especially the OpenIE-4 license, as it's particularly restrictive). The project requies Java 8 or higher. 

## Installation

Install dependencies via Maven using pom.xml. Note that we are using OpenIE-4, but OpenIE-5 has been released with minor changes. You can view new releases [here](https://search.maven.org/#search%7Cga%7C1%7Copenie).

## Purpose

OpenIE is a field of NLP that focuses on extracting tuples from text in the form:

'''
<subject, relation/action, object>
'''

For example:

'''
The dog runs to the house.
<the dog, runs to, the house>
'''

This contrasts traditional information extraction, which has a preset list of relations. 

We created this repo to informally benchmark rule-based OpenIE systems. We found them to be unsatisfactory for our use cases, and have started developing of Squadie (an open information extraction corpus based on SQuAD) and Nopie (a neural open information extractor).
