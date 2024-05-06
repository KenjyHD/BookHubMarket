import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { Container, Header, Image } from 'semantic-ui-react';
import {bookApi} from "../misc/BookApi";
import {useAuth} from "../context/AuthContext";

function BookDetails() {
    const { isbn } = useParams();
    const user = useAuth().getUser()
    const [book, setBook] = useState(null);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        const fetchBookDetails = async () => {
            try {
                const response = await bookApi.getBook(user, isbn)
                if (!response) {
                    throw new Error('Failed to fetch book details');
                }
                setBook(response.data);
                setIsLoading(false);
            } catch (error) {
                console.error('Error fetching book details:', error);
                setIsLoading(false);
            }
        };

        fetchBookDetails();
    }, [isbn]);

    if (isLoading) {
        return <Container>Loading...</Container>;
    }

    if (!book) {
        return <Container>No book found</Container>;
    }

    return (
        <Container>
            <Header as='h2'>{book.title}</Header>
            <Image src={`http://covers.openlibrary.org/b/isbn/${book.isbn}-L.jpg`} size='medium' bordered rounded />
            <p>ISBN: {book.isbn}</p>
            <p>Description: {book.description || 'No description available'}</p>
        </Container>
    );
}

export default BookDetails;
