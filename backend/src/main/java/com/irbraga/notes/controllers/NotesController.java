package com.irbraga.notes.controllers;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.irbraga.notes.dto.NoteDto;
import com.irbraga.notes.entities.NoteEntity;
import com.irbraga.notes.entities.TagEntity;
import com.irbraga.notes.repositories.NotesRepository;
import com.irbraga.notes.repositories.TagsRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Notes", description = "Endpoints related to Notes.")
@RestController("Nodes Controller")
@RequestMapping(path = "/notes", produces = MediaType.APPLICATION_JSON_VALUE)
public class NotesController {

    // Injected repository.
    @Autowired
    private NotesRepository notesRepository;
    @Autowired
    private TagsRepository tagsRepository;

    /**
     * Creates a Note.
     * @param dto
     * @return
     */
    @Operation(description = "Creates a note.", summary = "Creates a note.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Created.", content = {@Content(schema = @Schema(implementation = NoteReturnObject.class))}),
        @ApiResponse(responseCode = "400", description = "Bad request.", content = @Content)
    })
    @PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public NoteEntity create(@Valid @RequestBody NoteDto dto) {
        NoteEntity note = new NoteEntity();
        BeanUtils.copyProperties(dto, note);
        return notesRepository.save(note);
    }

    /**
     * Updates a note.
     * @param uuid
     * @param dto
     * @return
     */
    @Operation(description = "Updates a note.", summary = "Updates a note.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success.", content = {@Content(schema = @Schema(implementation = NoteReturnObject.class))}),
        @ApiResponse(responseCode = "400", description = "Bad request.", content = @Content),
        @ApiResponse(responseCode = "404", description = "Not found.", content = @Content)
    })
    @PutMapping(path = "/{uuid}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NoteEntity> update(@PathVariable UUID uuid, @Valid NoteDto dto) {
        Optional<NoteEntity> tag = notesRepository.findById(uuid);
        if (tag.isPresent()) {
            BeanUtils.copyProperties(dto, tag.get());
            return ResponseEntity.status(HttpStatus.OK).body(notesRepository.save(tag.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    /**
     * Retreives a note by it's ID.
     * @param uuid
     * @return
     */
    @Operation(description = "Retreives a note by it's ID.", summary = "Retreives a note by it's ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success.", content = {@Content(schema = @Schema(implementation = NoteReturnObject.class))}),
        @ApiResponse(responseCode = "400", description = "Bad request.", content = @Content)
    })
    @GetMapping(path = "/{uuid}")
    public Optional<NoteEntity> get(@PathVariable UUID uuid) {
        return notesRepository.findById(uuid);
    }
    
    /**
     * Retreives all notes.
     * @return
     */
    @Operation(description = "Retreives all notes.", summary = "Retreives all notes from database.")
    @ApiResponse(responseCode = "200", description = "Success.", content = @Content(array = @ArraySchema(schema = @Schema(implementation = NoteReturnObject.class))))
    @GetMapping(path = "/")
    public List<NoteEntity> list() {
        return notesRepository.findAll();
    }

    /**
     * Retreives all notes with a specific tag ID.
     * @param uuid
     * @param pageInfo
     * @return
     */
    @Operation(description = "Retreives all notes with a specific tag ID.", summary = "Retreives all notes with a specific tag ID.")
    @ApiResponse(responseCode = "200", description = "Success.", content = @Content(array = @ArraySchema(schema = @Schema(implementation = NoteReturnObject.class))))
    @GetMapping(path = "/tag/{uuid}")
    public List<NoteEntity> listByTagId(@PathVariable UUID uuid) {
        return notesRepository.findByTagsId(uuid);
    }

    /**
     * Deletes a note by it's ID.
     * @param uuid
     * @return
     */
    @Operation(description = "Deletes a note by it's ID.", summary = "Deletes a note by it's ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success.", content = @Content),
        @ApiResponse(responseCode = "404", description = "Not found.", content = @Content)
    })
    @DeleteMapping(path = "/{uuid}")
    public ResponseEntity<?> delete(@PathVariable UUID uuid) {
        Optional<NoteEntity> note = notesRepository.findById(uuid);
        if (note.isPresent()) {
            notesRepository.deleteById(uuid);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @Operation(description = "Add a tag to a note.", summary = "Add a tag to a note.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success.", content = {@Content(schema = @Schema(implementation = NoteReturnObject.class))}),
        @ApiResponse(responseCode = "404", description = "Not found.", content = @Content)
    })
    @PutMapping(path = "/{uuid}/tag/{tagUuid}")
    public ResponseEntity<NoteEntity> addTag(@PathVariable UUID uuid, @PathVariable UUID tagUuid) {
        Optional<NoteEntity> note = notesRepository.findById(uuid);
        Optional<TagEntity> tag = tagsRepository.findById(tagUuid);

        if (note.isPresent() && tag.isPresent()) {
            if (!note.get().getTags().contains(tag.get())) {
                note.get().getTags().add(tag.get());
                return ResponseEntity.status(HttpStatus.OK).body(notesRepository.save(note.get()));
            }
            return ResponseEntity.status(HttpStatus.OK).body(note.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @Operation(description = "Remove a tag from a note.", summary = "Remove a tag from a note.")
    @DeleteMapping(path = "/{uuid}/tag/{tagUuid}")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success.", content = {@Content(schema = @Schema(implementation = NoteReturnObject.class))}),
        @ApiResponse(responseCode = "404", description = "Not found.", content = @Content)
    })
    public ResponseEntity<NoteEntity> removeTag(@PathVariable UUID uuid, @PathVariable UUID tagUuid) {
        Optional<NoteEntity> note = notesRepository.findById(uuid);
        Optional<TagEntity> tag = tagsRepository.findById(tagUuid);

        if (note.isPresent() && tag.isPresent()) {
            if (note.get().getTags().contains(tag.get())) {
                note.get().getTags().remove(tag.get());
                return ResponseEntity.status(HttpStatus.OK).body(notesRepository.save(note.get()));
            }
            return ResponseEntity.status(HttpStatus.OK).body(note.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}

/**
 * Record class to help document the services in swagger ui.
 */
record NoteReturnObject(UUID id, String title, String body, Date createdAt, Date updatedAt, List<TagReturnObject> tags) {}
