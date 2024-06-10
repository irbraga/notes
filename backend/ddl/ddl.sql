
-- Notes table
CREATE TABLE public.notes (
	note_id UUID NOT NULL,
	title varchar(50),
	body text NULL,
	color bpchar(7) DEFAULT '#FFFFFF'::bpchar NOT NULL,
	created_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	CONSTRAINT notes_pkey PRIMARY KEY (note_id)
);

-- Constraint to check if either title or body is not empty/null 
alter table public.notes add constraint chk_title_or_body_null_empty check ( coalesce(trim(title), '') = '' is false or coalesce(trim(body), '') = '' is false );

-- Tags table
CREATE TABLE public.tags (
	tag_id UUID NOT NULL,
	"name" varchar(50) NOT NULL,
	created_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	CONSTRAINT tag_name_uk UNIQUE (name),
	CONSTRAINT tags_pkey PRIMARY KEY (tag_id)
);
-- Index to check if a tag name is unique ignoring case
CREATE UNIQUE INDEX tag_name_idx ON public.tags USING btree (TRIM(BOTH FROM lower((name)::text)));

-- Relationship table between Notes x Tags
CREATE TABLE public.notes_tags (
	note_id UUID NOT NULL,
	tag_id UUID NOT NULL
);
-- Adding constraints
ALTER TABLE public.notes_tags ADD CONSTRAINT note_fk_tag_fk_uk check (note_id, tag_id) UNIQUE;