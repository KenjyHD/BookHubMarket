import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Container, Header, List, Button, Loader, Message } from 'semantic-ui-react';
import { bookApi } from "../misc/BookApi";
import { useAuth } from "../context/AuthContext";

function PersonalLibrary() {
  const Auth = useAuth()
  const user = Auth.getUser()
  const [books, setBooks] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchPurchasedBooks = async () => {
      try {
        const response = await bookApi.getPurchasedBooks(user);
        setBooks(response.data);
      } catch (error) {
        console.error('Error fetching purchased books:', error);
        setError('Failed to fetch purchased books. Please try again later.');
      } finally {
        setIsLoading(false);
      }
    };

    fetchPurchasedBooks();
  }, [user.id]);

  const handleDownload = async (bookId) => {
    try {
      const response = await bookApi.downloadBook(user, bookId);
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', `book_${bookId}.pdf`);
      document.body.appendChild(link);
      link.click();
      link.parentNode.removeChild(link);
    } catch (error) {
      console.error('Error downloading book:', error);
      setError('Failed to download the book. Please try again later.');
    }
  };

  if (isLoading) {
    return (
      <Container textAlign='center'>
        <Loader active inline='centered'>Loading...</Loader>
      </Container>
    );
  }

  if (error) {
    return (
      <Container textAlign='center'>
        <Message negative>
          <Message.Header>Error</Message.Header>
          <p>{error}</p>
        </Message>
      </Container>
    );
  }

  return (
    <Container>
      <Header as='h2' textAlign='center'>My Library</Header>
      <List divided relaxed>
        {books.map(book => (
          <List.Item key={book.id}>
            <List.Content floated='right'>
              <Button onClick={() => handleDownload(book.id)}>Download PDF</Button>
              <Button as={Link} to={`/books/${book.id}`}>View Details</Button>
            </List.Content>
            <List.Content>
              <List.Header>{book.title}</List.Header>
              <List.Description>{book.author}</List.Description>
            </List.Content>
          </List.Item>
        ))}
      </List>
    </Container>
  );
}

export default PersonalLibrary;
