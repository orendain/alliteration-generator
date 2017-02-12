# Alliteration Generator

A dump of my work with the alliteration generator.  _**Incomplete codebase due to a systems failure.**_  Who would have thought two backup sources would have failed simultaneously? ... :(

Unfortunately, this codebase was a few months behind the released application.  Last application release was sometime around 2013.

The Alliteration Generator generates random, arbitrarily long, grammatically correct, alliterative sentences.

--

What is allitgen?

Formulate sets of rules, following dependency/constituent grammar theories.

Generate sentences based on these rules.

Breaking a sentence down is hard:  Silly kids jump on houses.  NP | VP = (V | PP)

Easy to tell which is NP/VP/PP if sentences are broken up into units.  Hard to tell what phrases are units to begin with.

Some strategies include grouping words into types or classes and then using valency.

As great as we are at inducing grammar, we can't get very specific.  Is the VP progressive present?  Indicative future?  Past perfect?

Grammar is very interesting, because the relationship between syntactic units forms a web that's hard to categorize.  It's centralized, decentralized and distributed on different levels.

We can binarize a sytax tree so that it appears centralized, but when considering what phrases and classes of words make up other phrases, it's best visualized as a web.

My goal is to generate constituency(and eventually dependency)-based grammar webs that can be traversed to formulate comlpex, comprehensible and grammatically-correct syntax trees and sentences.

The units would not just be phrasal categories as constituency-based NLP libraries are able to induce from text, but more lexical, as dependency grammar acknowledges.  It also incorporates mood and tense.

Statistical parsers do a great job at lexical categorization, but as far as I know there exists no comprehensive knowledge base that maps grammar with


Idea:
House grammar webs constructed through the grammar banks generated by different NLP groups.  Sentences can be fed into other parsers to see which ones parsers work well parsing each others sentences.

Controlled (hand-fixed) grammar would be the one developed by hand.

Eventually, it would be great if an extension could be developed to a parser that takes feedback based on how it parsed a sentence fed into it.  Feedback routines exist, would just need to plug into them.