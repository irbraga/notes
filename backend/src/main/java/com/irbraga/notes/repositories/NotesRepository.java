package com.irbraga.notes.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.irbraga.notes.entities.NoteEntity;

@Repository
public interface NotesRepository extends JpaRepository<NoteEntity, UUID>{

    public List<NoteEntity> findByTagsId(UUID uuid);
}
