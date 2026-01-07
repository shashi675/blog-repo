package com.project.journalApp.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.project.journalApp.entity.JournalEntry;
import com.project.journalApp.entity.User;
import com.project.journalApp.repository.JournalEntryRepository;

@Component
public class JournalEntryService {
	
	@Autowired
	private JournalEntryRepository journalEntryRepository;
	
	@Autowired
	private UserService userService;

	public List<JournalEntry> getAll(String userName) {
		User user = userService.getByUserName(userName);
		return user.getJournalEntries();
	}
	
	@Transactional
//	for transaction management: if something breaks, error is thrown, we should not catch it in the same method, it should be thrown to consider whole method as single transaction (atomicity).
	public void saveEntry(JournalEntry journalEntry, String userName) {
        journalEntry.setDate(LocalDateTime.now());
        JournalEntry savedEntry = journalEntryRepository.save(journalEntry);
        User user = userService.getByUserName(userName);
        user.getJournalEntries().add(savedEntry);
        userService.saveExistingUser(user);
    }

	public JournalEntry getById(ObjectId id, String userName) {
		User user = userService.getByUserName(userName);
		List<JournalEntry> collectedEntries = user.getJournalEntries().stream()
				.filter(x -> x.getId().equals(id))
				.toList();

		if(!collectedEntries.isEmpty()) {
			return collectedEntries.get(0);
		}
		return null;
	}
	
	@Transactional
	public boolean deleteById(ObjectId id, String userName) {
		boolean isRemoved = false;
		try {
			User user = userService.getByUserName(userName);
			isRemoved = user.getJournalEntries().removeIf(x -> x.getId().equals(id));

			if(isRemoved) {
				journalEntryRepository.deleteById(id);
				userService.saveExistingUser(user);
			}
		} catch (Exception e) {
			System.out.println(e);
			throw new RuntimeException("An error occured during deleting an entry. ", e);
		}
		return isRemoved;
	}

	public JournalEntry updateEntry(
			ObjectId journalId, JournalEntry journalEntry, String userName) {

		User user = userService.getByUserName(userName);
		List<JournalEntry> collectedEntries = user.getJournalEntries().stream()
				.filter(x -> x.getId().equals(journalId))
				.toList();

		if(!collectedEntries.isEmpty()) {
			Optional<JournalEntry> savedEntryOptional = journalEntryRepository.findById(journalId);
			if(savedEntryOptional.isPresent()) {
				JournalEntry savedEntry = savedEntryOptional.get();
				savedEntry.setTitle(!journalEntry.getTitle().isEmpty() ? journalEntry.getTitle() : savedEntry.getTitle());
				savedEntry.setContent(journalEntry.getContent() != null && !journalEntry.getContent().isEmpty() ? journalEntry.getContent() : savedEntry.getContent());
				journalEntryRepository.save(savedEntry);
				return savedEntry;
			}
		}
		return null;
	}
}
