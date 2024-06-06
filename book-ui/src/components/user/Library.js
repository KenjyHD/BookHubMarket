import React, { useEffect, useState } from 'react';
import { useLocation, Link } from 'react-router-dom';
import { Container, Header, Form, Icon, Image, Input, Item, Segment, Popup, Grid, Loader, Message } from 'semantic-ui-react';
import { useAuth } from '../context/AuthContext';
import { bookApi } from '../general/BookApi';
import { handleLogError } from '../general/Helpers';

function Library() {
    const location = useLocation();
    const Auth = useAuth();
    const user = Auth.getUser();

    const [books, setBooks] = useState([]);
    const [bookTextSearch, setBookTextSearch] = useState('');
    const [isBooksLoading, setIsBooksLoading] = useState(false);
    const [error, setError] = useState(null);

    useEffect(() => {
        handleGetBooks();
    }, [location.pathname]);

    const handleInputChange = (e, { name, value }) => {
        if (name === 'bookTextSearch') {
            setBookTextSearch(value);
        }
    };

    const handleGetBooks = async () => {
        try {
            setIsBooksLoading(true);
            const response = location.pathname === '/personal-library'
                ? await bookApi.getPurchasedBooks(user)
                : await bookApi.getBooks(user);
            let books = response.data;
            books = books.sort((a, b) => a.title.localeCompare(b.title));
            setBooks(books);
        } catch (error) {
            handleLogError(error);
            setError('Failed to fetch books. Please try again later.');
        } finally {
            setIsBooksLoading(false);
        }
    };

    const handleSearchBook = async () => {
        try {
            const response = location.pathname === '/personal-library'
                ? await bookApi.getPurchasedBooks(user, bookTextSearch)
                : await bookApi.getBooks(user, bookTextSearch);
            let books = response.data;
            books = books.sort((a, b) => a.title.localeCompare(b.title));
            setBooks(books);
        } catch (error) {
            handleLogError(error);
            setBooks([]);
        }
    };

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

    if (isBooksLoading) {
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

    let bookList;
    if (books.length === 0) {
        bookList = <Item key='no-book'>No book</Item>;
    } else {
        bookList = books.map(book => (
            <Item key={book.id}>
                <Image src={`http://localhost:8080/file-storage/book-covers/${book.coverId}`} size='tiny' bordered rounded />
                <Item.Content>
                    <Item.Header as={Link} to={`/book/${book.id}`}>{book.title}</Item.Header>
                    <Item.Description>
                        <p>{book.description}</p>
                        <p><strong>Genre:</strong> {book.genre || 'Not specified'}</p>
                        <p><strong>Price:</strong> ${book.price.toFixed(2)}</p>
                    </Item.Description>
                </Item.Content>
                <Item.Image>
                    <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'space-between', height: '100%' }}>
                        <Popup content='View Details' trigger={
                            <Link to={`/book/${book.id}`}>
                                <Icon name='eye' size='big' style={{ marginTop: '10px' }} />
                            </Link>
                        } />
                        {location.pathname === '/personal-library' &&
                            <Popup content='Download PDF' trigger={
                                <Icon name='download' size='big' style={{ marginBottom: '10px' }} onClick={() => handleDownload(book.id)} />
                            } />
                        }
                    </div>
                </Item.Image>
            </Item>
        ));
    }

    return (
        <Container>
            <Segment loading={isBooksLoading} color='blue'>
                <Grid stackable divided>
                    <Grid.Row columns='2'>
                        <Grid.Column width='3'>
                            <Header as='h2'>
                                <Icon name='book' />
                                <Header.Content>{location.pathname === '/personal-library' ? 'My Books' : 'Books'}</Header.Content>
                            </Header>
                        </Grid.Column>
                        <Grid.Column>
                            <Form onSubmit={handleSearchBook}>
                                <Input
                                    action={{ icon: 'search' }}
                                    name='bookTextSearch'
                                    placeholder='Search by Title'
                                    value={bookTextSearch}
                                    onChange={handleInputChange}
                                />
                            </Form>
                        </Grid.Column>
                    </Grid.Row>
                </Grid>
                <Item.Group divided unstackable relaxed link>
                    {bookList}
                </Item.Group>
            </Segment>
        </Container>
    );
}

export default Library;
