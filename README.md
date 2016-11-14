# NucleosomePatternClassifier
This project aims to evaluate different representations in genomic sequence classification.
We focus on the nucleosome binding setting.

## Abstract
Knowing the precise locations of nucleosomes in a genome is key to understanding how genes are regulated.
Genomic text-mining defined as the automatic extraction of information about genes from text forms
an interdisciplinary field which brings together the disciplines of machine learning, bioinformatics 
and computational linguistics. The use of machine learning tools in biological studies , due to the
affinity of the former for largescale dataheavy experiments, has increased the pace at which information
is produced. Consequently, during the latest years, an increasing interest has emerged, in applying textmining
techniques in genomic studies, an example of which is examining whether the primary structure of DNA,
i.e. textual data extracted from genomes, influences nucleosome positioning and, thus, chromatin
structure. To the best of our knowledge, there exists no complete study on the effect of representation to 
the classification of genomic sequenses as **nucleosomefree regions** (*NFR*) or **nucleosome binding sites** (*NBS*). 
In this approach we study 3 different genomic sequence representations (**Hidden Markov Models, Bag-of-Words**
and **N-gram Graphs**) in combination to a number of machine learning algorithms on the task of classifying 
genomic sequences as *NFR* and *NBS*. Finally, we conclude that, based on our findings, novel approaches may
be more suitable for defining the structural elements of chromatin, as they prove to be more effective at 
predicting nucleosome positioning based on the textual data of the underlying genomic sequence.

## License
The code is released under Apache v2.0 license.
