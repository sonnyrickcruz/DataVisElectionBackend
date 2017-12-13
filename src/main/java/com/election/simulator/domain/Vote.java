package com.election.simulator.domain;

public class Vote {
	private int voteId;
	private Candidate candidate;
	private Voter voter;

	public int getVoteId() {
		return voteId;
	}

	public void setVoteId(int voiteId) {
		this.voteId = voiteId;
	}
	
	public Candidate getCandidate() {
		return candidate;
	}

	public void setCandidate(Candidate candidate) {
		this.candidate = candidate;
	}

	public Voter getVoter() {
		return voter;
	}

	public void setVoter(Voter voter) {
		this.voter = voter;
	}
}
