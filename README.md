# NucleosomePatternClassifier
This project aims to evaluate different representations in genomic sequence classification.
We focus on the nucleosome binding setting.

## Abstract
Knowing the precise locations of nucleosomes in a genome is key to understanding how genes are regulated.
Genomic text-mining defined as the automatic extraction of information about genes from text forms
an interdisciplinary field which brings together the disciplines of machine learning, bioinformatics 
and computational linguistics. The use of machine learning tools in biological studies , due to the
affinity of the former for largescale dataheavy experiments, has increased the pace at which information
is produced.

Consequently, during the latest years, an increasing interest has emerged, in applying textmining
techniques in genomic studies, an example of which is examining whether the primary structure of DNA,
i.e. textual data extracted from genomes, influences nucleosome positioning and, thus, chromatin
structure. To the best of our knowledge, there exists no complete study on the effect of representation to 
the classification of genomic sequenses as **nucleosome free regions** (*NFR*) or **nucleosome binding sites** (*NBS*). 

In this approach we study 3 different genomic sequence representations (**Hidden Markov Models, Bag-of-Words**
and  **N-gram Graphs**) in combination to a number of machine learning algorithms on the task of classifying 
genomic sequences as *NFR* and *NBS*. Finally, we conclude that, based on our findings, novel approaches may
be more suitable for defining the structural elements of chromatin, as they prove to be more effective at 
predicting nucleosome positioning based on the textual data of the underlying genomic sequence.

## Experiments
The dataset we will use for our study consists of the the S. cerevisiae genome and is the
similar dataset to that used in .Due to it being a commonly used dataset in previous
studies, we can easily compare our results with that of previous experiments.

Here, follows a description of the data files :
* **.bed files**: These .bed files (browser extensible data) have a quite simple structure, which consists from three elements : 
a) the chromosome to which the specific data belongs, 
b) the starting point, and 
c) the ending point of it.
For example the line "chr5 100 200" means that the element we’ve encountered belongs to the 5th
chromosome from the 100th position to the 200th.
* **.fa files**: These .fa files complement the above .bed files by providing more information for the specific coordinated 
elements, due to having not only the information about the three elements described above, but also having 
information about the nucleotide sequences. Hence, the above example now looks like this: "chr5:100200
ATGAGA..."

In our approach we use exclusively the .fa files (FASTA). The NBS and NFR data files in both formats can be found in the Datasets
folder of this repository.

## License
The code is released under Apache v2.0 license.
