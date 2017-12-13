package com.election.simulator.domain;

import java.util.ArrayList;
import java.util.List;

public class Votes {
	private Candidate candidate;
	private List<Vote> votes;

	public Candidate getCandidate() {
		return candidate;
	}

	public void setCandidate(Candidate candidate) {
		this.candidate = candidate;
	}

	public List<Vote> getVotes() {
		if (votes == null) {
			votes = new ArrayList<>();
		}
		return votes;
	}

	public void setVotes(List<Vote> votes) {
		this.votes = votes;
	}
}
