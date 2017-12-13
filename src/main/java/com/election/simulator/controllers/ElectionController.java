package com.election.simulator.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import com.election.simulator.domain.Candidate;
import com.election.simulator.domain.Party;
import com.election.simulator.domain.Vote;
import com.election.simulator.domain.Votes;
import com.election.simulator.domain.VotesSummary;

@Controller
public class ElectionController {
	private static final Logger logger = LoggerFactory.getLogger(ElectionController.class);
	
	public static int hits = 0;
	
	public Map<Integer, Candidate> candidates = new HashMap<>();
	
	public Map<Integer, Party> parties = new HashMap<>();
	
	public Map<Integer, Votes> voteMap = new HashMap<>();
	
	public Map<Integer, VotesSummary> voteSummaryMap = new HashMap<>();
	
	private boolean isThreading = false;
	
	@Autowired
	private SimpMessageSendingOperations messagingTemplate;
	
	@MessageMapping("connect")
	public void connect(SimpMessageHeaderAccessor accessor) throws InterruptedException {
		Thread.sleep(700);
		logger.debug("Someone's connected");
		logger.info(++hits + " user/s are connected.");
		Thread.sleep(700);
		initCandidate();
		if (!isThreading)
			sendMessage();
	}
	
	public synchronized void sendMessage() {
		isThreading = true;
		while(true) {
			try {
				Thread.sleep(5000);
				simulateVotes();
				messagingTemplate.convertAndSend("/client/election", voteSummaryMap);
			} catch (InterruptedException e) {
				logger.debug("send message thread interupted.");
				isThreading = false;
				break;
			}
		}
	}
	
	public void simulateVotes() {
		int min = 1;
		int maxProcess = 10;
		int max = 3;
		int randomNum = ThreadLocalRandom.current().nextInt(min, maxProcess + 1);
		Vote vote;
		for (int i=0; i < 10; i ++) {
			randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);
			vote = new Vote();
			vote.setCandidate(getCandidate(randomNum));
			vote.setVoter(null);
			addVote(vote);			
		}
		for (Integer key : voteMap.keySet()) {
			VotesSummary votesSummary = new VotesSummary();
			votesSummary.setCandidate(voteMap.get(key).getCandidate());
			votesSummary.setVoteCount(voteMap.get(key).getVotes().size());
			voteSummaryMap.put(key, votesSummary);
		}
	}
	
	public void addVote(Vote vote) {
		int candidateId = vote.getCandidate().getCandidateId();
		if (!voteMap.containsKey(candidateId)) {
			Votes votes = new Votes();
			votes.setCandidate(getCandidate(candidateId));
			voteMap.put(candidateId, votes);
		}
		voteMap.get(candidateId).getVotes().add(vote);
	}
	
	public Candidate getCandidate(int id) {
		if (candidates.size() == 0) {
			initCandidate();
		}
		return candidates.get(id);
	}
	
	public void initCandidate() {
		Candidate candidate = new Candidate();
		candidate.setParty(parties.get(1));
		candidate.setFirstName("Rodrigo");
		candidate.setLastName("Duterte");
		candidate.setCandidateId(1);
		candidate.setParty(getParty(1));
		candidates.put(candidate.getCandidateId(), candidate);
		
		candidate = new Candidate();
		candidate.setParty(parties.get(2));
		candidate.setFirstName("Mar");
		candidate.setLastName("Roxas");
		candidate.setCandidateId(2);
		candidate.setParty(getParty(2));
		candidates.put(candidate.getCandidateId(), candidate);
		
		candidate = new Candidate();
		candidate.setParty(parties.get(3));
		candidate.setFirstName("Jejomar");
		candidate.setLastName("Binay");
		candidate.setCandidateId(3);
		candidate.setParty(getParty(3));
		candidates.put(candidate.getCandidateId(), candidate);
	}
	
	public void initParty() {
		Party party = new Party();
		party.setPartyId(1);
		party.setColor("#c0392b");
		party.setName("PDP Laban");
		parties.put(party.getPartyId(), party);
		
		party = new Party();
		party.setPartyId(2);
		party.setColor("#f1c40f");
		party.setName("Liberal");
		parties.put(party.getPartyId(), party);

		party = new Party();
		party.setPartyId(3);
		party.setColor("#2196F3");
		party.setName("United Nationalist Alliance");
		parties.put(party.getPartyId(), party);
	}
	
	public Party getParty(int id) {
		if (parties.size() == 0) {
			initParty();
		}
		return parties.get(id);
	}
}
