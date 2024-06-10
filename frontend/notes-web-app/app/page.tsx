export default async function Home() {
  // const tags = await fetchData("http://localhost:8080/tags/");
  const notes = await fetchData("http://localhost:8080/notes/");

  async function fetchData(url: string) {
    return fetch(url)
      .then((resp) => resp.json())
      .catch((error) => console.log(error))
      .then((data) => {
        console.log(data);
        return data;
      });
  }

  return (
    <div>
      <h1>Notes</h1>
      {notes.map((note: any) => {
        return (
          <div key={note.id}>
            <h3>{note.title}</h3>
            <h5>{note.body}</h5>
            <h6>Created at: {note.createdAt}</h6>
            <h6>Updated at: {note.updatedAt}</h6>
            <ul>
              {note.tags.map((tag: any) => {
                return <li key={tag.id}>{tag.name}</li>;
              })}
            </ul>
          </div>
        );
      })}
      {/* <h1>Tags</h1>
      {tags.map((tag: any) => {
        return (
          <div key={tag.id}>
            <h3>{tag.name}</h3>
            <h6>Created at: {tag.createdAt}</h6>
            <h6>Updated at: {tag.updatedAt}</h6>
          </div>
        );
      })} */}
    </div>
  );
}
