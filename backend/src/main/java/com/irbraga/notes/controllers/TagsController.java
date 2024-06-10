/**
 * Controller class to handle Tag entity functionalities.
 */
package com.irbraga.notes.controllers;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

import com.irbraga.notes.dto.TagDto;
import com.irbraga.notes.entities.TagEntity;
import com.irbraga.notes.repositories.TagsRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@Tag(name = "Tags", description = "Endpoints related to Tags.")
@RestController("Tags Controller")
@RequestMapping(path = "/tags", produces = MediaType.APPLICATION_JSON_VALUE)
public class TagsController {

    // Injected repository.
    @Autowired
    private TagsRepository tagsRepository;

    /**
     * Creates a Tag.
     * @param dto
     * @return
     */
    @Operation(description = "Creates a tag.", summary = "Creates a tag.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Created.", content = {@Content(schema = @Schema(implementation = TagReturnObject.class))}),
        @ApiResponse(responseCode = "400", description = "Bad request.", content = @Content)
    })
    @PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TagEntity> create(@Valid @RequestBody TagDto dto) {
        TagEntity tag = new TagEntity();
        BeanUtils.copyProperties(dto, tag);
        return ResponseEntity.status(HttpStatus.CREATED).body(tagsRepository.save(tag));
    }
    
    /**
     * Updates a Tag.
     * @param uuid
     * @param dto
     * @return
     */
    @Operation(description = "Updates a tag.", summary = "Updates a tag.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success.", content = {@Content(schema = @Schema(implementation = TagReturnObject.class))}),
        @ApiResponse(responseCode = "400", description = "Bad request.", content = @Content),
        @ApiResponse(responseCode = "404", description = "Not found.", content = @Content)
    })
    @PutMapping(path = "/{uuid}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TagEntity> update(@PathVariable UUID uuid, @Valid @RequestBody TagDto dto) {
        Optional<TagEntity> tag = tagsRepository.findById(uuid);
        if (tag.isPresent()) {
            BeanUtils.copyProperties(dto, tag.get());
            return ResponseEntity.status(HttpStatus.OK).body(tagsRepository.save(tag.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    /**
     * Returns a page os Tag objects.
     * @param pageInfo
     * @return
     */
    @Operation(description = "Retreives a page of tags.", summary = "Retreives a page of tags.")
    @ApiResponse(responseCode = "200", description = "Success.", content = @Content)
    @GetMapping(path = "/paginate")
    public Page<TagEntity> paginate(@PageableDefault(page = 0, sort = {"name"}, direction = Sort.Direction.ASC, size = 10) Pageable pageInfo) {
        return tagsRepository.findAll(pageInfo);
    }

    /**
     * Lists all tags.
     * @return
     */
    @Operation(description = "Retreives all tags.", summary = "Retreives all tags.")
    @ApiResponse(responseCode = "200", description = "Success.", content = @Content(array = @ArraySchema(schema = @Schema(implementation = TagReturnObject.class))))
    @GetMapping(path = "/")
    public List<TagEntity> list() {
        return tagsRepository.findAll();
    }

    /**
     * Gets a tag by it's ID.
     * @param uuid
     * @return
     */
    @Operation(description = "Retreives a tag by it's ID.", summary = "Retreives a tag by it's ID.")
    @ApiResponse(responseCode = "200", description = "Success.", content = @Content(schema = @Schema(implementation = TagReturnObject.class)))
    @GetMapping(path = "/{uuid}")
    public Optional<TagEntity> get(@PathVariable UUID uuid) {
        return tagsRepository.findById(uuid);
    }

    /**
     * Deletes a tag if exists.
     * @param uuid
     * @return
     */
    @Operation(description = "Deletes a tag by it's ID.", summary = "Deletes a tag by it's ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success", content = @Content),
        @ApiResponse(responseCode = "404", description = "Not found.", content = @Content)
    })
    @DeleteMapping(path = "/{uuid}")
    public ResponseEntity<?> delete(@PathVariable UUID uuid) {
        Optional<TagEntity> note = tagsRepository.findById(uuid);
        if (note.isPresent()) {
            tagsRepository.deleteById(uuid);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}

/**
 * Record class to help document the services in swagger ui.
 */
record TagReturnObject(UUID id, String name, Date createdAt, Date updatedAt) {}
