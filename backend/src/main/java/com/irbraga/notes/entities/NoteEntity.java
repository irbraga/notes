package com.irbraga.notes.entities;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
@Entity
@Table(name = "notes")
public class NoteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "note_id")
    private UUID id;

    @Length(min = 1, max = 50)
    @Column(name = "title", nullable = true, length = 50)
    private String title;

    @Column(name = "body", nullable = true)
    private String body;

    @Length(min = 7, max = 7)
    @Column(name = "color", nullable = false, length = 7, columnDefinition = "bpchar")
    private String color;

    @Column(name = "created_at", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "updated_at", insertable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updatedAt;

    @JsonManagedReference
    @ManyToMany(fetch = FetchType.EAGER,
                cascade = CascadeType.ALL)
    @JoinTable(name = "notes_tags",
        joinColumns = {@JoinColumn(name = "note_id")},
        inverseJoinColumns = {@JoinColumn(name = "tag_id")})
    private List<TagEntity> tags;

    /**
     * Setting default values.
     */
    @PrePersist
    public void prePersist() {
        if (this.color == null) {
            this.color = "#FFFFFF";
        }
    }
}
