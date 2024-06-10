package com.irbraga.notes.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.irbraga.notes.entities.TagEntity;

public interface TagsRepository extends JpaRepository<TagEntity, UUID>{
}
