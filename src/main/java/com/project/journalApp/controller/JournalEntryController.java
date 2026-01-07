package com.project.journalApp.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import com.project.journalApp.entity.JournalEntry;
import com.project.journalApp.entity.User;
import com.project.journalApp.service.JournalEntryService;
import com.project.journalApp.service.UserService;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/journal")
@AllArgsConstructor
public class JournalEntryController {
	
	private JournalEntryService journalEntryService;
	
		
	@GetMapping
	public ResponseEntity<?> getAllJournalEntriesOfUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userName = authentication.getName();
		List<JournalEntry> journalEntries = journalEntryService.getAll(userName);
		return new ResponseEntity<>(journalEntries, HttpStatus.OK);
	}


	@PostMapping
	public ResponseEntity<?> createJournal(@RequestBody JournalEntry journalEntry) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String userName = authentication.getName();
			journalEntryService.saveEntry(journalEntry, userName);
			return new ResponseEntity<>(journalEntry, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("id/{journalId}")
	public ResponseEntity<?> getJournalById(@PathVariable ObjectId journalId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userName = authentication.getName();
		JournalEntry entry = journalEntryService.getById(journalId, userName);
		if(entry != null) {
			return new ResponseEntity<>(entry, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}


	@DeleteMapping("id/{journalId}")
	public ResponseEntity<?> deleteById(@PathVariable ObjectId journalId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userName = authentication.getName();
		boolean isRemoved = journalEntryService.deleteById(journalId, userName);
		if(isRemoved)
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}


	@PutMapping("id/{journalId}")
	public ResponseEntity<JournalEntry> updateJournalById(
			@PathVariable ObjectId journalId, @RequestBody JournalEntry journalEntry) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userName = authentication.getName();
		JournalEntry updatedEntry = journalEntryService.updateEntry(journalId, journalEntry, userName);
		if(updatedEntry != null) {
			return new ResponseEntity<>(updatedEntry, HttpStatus.CREATED);
		}
		else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
}
