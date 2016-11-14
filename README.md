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
similar dataset to that used in [1].Due to it being a commonly used dataset in previous
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

## Tools Used
For handling the **Hidden Markov Model** part of the implementation, **Jahmm**[2], a Java library implementing the various algorithms 
related to HMMs was used. For training our HMM model, we used the *Baum-Welch* algorithm[3], which was implemented
in this library.
**Jahmm**’s original author is *JeanMarc Francois*.

In addition, for the **N-gram Graph** part of the implementation, the **JInsect**[4] toolkit was used. **JInsect** is a Java-based
toolkit and library that supports and demonstrates the use of n-gram graphs within Natural Language Processing applications.
For our implementation, we took advantage of **JInsect**’s tools capable of creating, merging and comparing N-gram graphs, which 
facilitated the feature extraction needed for the experiments.
**JInsect** was written by *George Giannakopoulos* and *Panagiotis Giotis*.

Finally, we used **Weka**[5][6] a collection of machine learning algorithms for data mining tasks, in order to utilize these 
algorithms for our classification tasks.
The **Weka Data Mining Software** was implemented by the *Machine Learning Group* at the *University of Waikato*.

## References
- [1] *Christoforos Nikolaou, Sonja Althammer, Miguel Beato, and Roderic Guig´o. Structural constraints revealed in consistent nucleosome positions in the genome of s. cerevisiae. 3(1):20.*
- [2] *https://github.com/aubry74/Jahmm*
- [3] *Leonard E. Baum and Ted Petrie. Statistical inference for probabilistic functions of finite state markov chains. Ann. Math. Statist., 37(6):1554–1563, 12 1966.*
- [4] *https://sourceforge.net/p/jinsect/wiki/Home/*
- [5] *http://www.cs.waikato.ac.nz/ml/weka/* 
- [6] *Mark Hall, Eibe Frank, Geoffrey Holmes, Bernhard Pfahringer, Peter Reutemann, and Ian H. Witten. The weka data mining software: An update. SIGKDD Explor. Newsl., 11(1):10–18, November 2009.*
## License
The code is released under Apache v2.0 license.
