package com.example.videoforensicanalysis.model;

public class Corpus {
	long corpusId;
	String corpusText;

	public Corpus() { }

	public Corpus(String corpusText) {
		super();
		this.corpusText = corpusText;
	}

	public void setCorpusId(long corpusId) {
		this.corpusId = corpusId;
	}
}