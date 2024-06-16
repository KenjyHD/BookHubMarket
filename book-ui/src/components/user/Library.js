import React, { useEffect, useState } from 'react';
import { useLocation, Link } from 'react-router-dom';
import {
    Container,
    Header,
    Form,
    Icon,
    Image,
    Item,
    Segment,
    Popup,
    Grid,
    Loader,
    Message,
    Button, Dropdown
} from 'semantic-ui-react';
import { useAuth } from '../context/AuthContext';
import { bookApi } from '../general/BookApi';
import { handleLogError } from '../general/Helpers';

function Library() {
    const location = useLocation();
    const user = useAuth().getUser();

    const [books, setBooks] = useState([]);
    const [searchParams, setSearchParams] = useState({
        title: '',
        genre: '',
        author: '',
        minPrice: '',
        maxPrice: ''
    });
    const [isBooksLoading, setIsBooksLoading] = useState(false);
    const [error, setError] = useState(null);
    const [filter, setFilter] = useState('all');

    useEffect(() => {
        handleGetBooks();
    }, [location.pathname, filter]);

    const handleInputChange = (e, { name, value }) => {
        setSearchParams(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    const handleGetBooks = async () => {
        try {
            setIsBooksLoading(true);
            let response;

            if (location.pathname === '/personal-library') {
                if (filter === 'purchased') {
                    response = await bookApi.getPurchasedBooks(user);
                } else if (filter === 'posted' && user.role === 'AUTHOR') {
                    response = await bookApi.getAuthorBooks(user);
                } else {
                    const purchasedBooksResponse = await bookApi.getPurchasedBooks(user);
                    const authoredBooksResponse = user.role === 'AUTHOR' ? await bookApi.getAuthorBooks(user) : { data: [] };
                    response = { data: [...purchasedBooksResponse.data, ...authoredBooksResponse.data] };
                }
            } else {
                response = await bookApi.getBooks(user);
            }

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
            const { title, genre, author, minPrice, maxPrice } = searchParams;
            let response;

            if (location.pathname === '/personal-library') {
                if (filter === 'purchased') {
                    response = await bookApi.getPurchasedBooks(user, title, genre, author, minPrice, maxPrice);
                } else if (filter === 'posted' && user.role === 'AUTHOR') {
                    response = await bookApi.getAuthorBooks(user, title, genre, author, minPrice, maxPrice);
                } else {
                    const purchasedBooksResponse = await bookApi.getPurchasedBooks(user, title, genre, author, minPrice, maxPrice);
                    const authoredBooksResponse = user.role === 'AUTHOR' ? await bookApi.getAuthorBooks(user, title, genre, author, minPrice, maxPrice) : { data: [] };
                    response = { data: [...purchasedBooksResponse.data, ...authoredBooksResponse.data] };
                }
            } else {
                response = await bookApi.getBooks(user, title, genre, author, minPrice, maxPrice);
            }

            let books = response.data;
            books = books.sort((a, b) => a.title.localeCompare(b.title));
            setBooks(books);
        } catch (error) {
            handleLogError(error);
            setBooks([]);
        }
    };

    const handleBookContentDownload = async (book) => {
        try {
            const response = await bookApi.downloadBook(user, book.id);
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', book.contentFilename);
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
                        <p><strong>Author:</strong> {book.author}</p>
                    </Item.Description>
                </Item.Content>
                <Item.Image>
                    <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'space-between', height: '100%' }}>
                        <Popup content='View Details' trigger={
                            <Link to={`/book/${book.id}`}>
                                <Icon name='eye' size='big' style={{ marginTop: '10px' }} />
                            </Link>
                        } />
                        {location.pathname === '/personal-library' && (
                            <>
                                {((user.role === 'ADMIN') || (user.role === 'AUTHOR' && book.authorId === user.id)) && (
                                    <Popup content='Edit Book' trigger={
                                        <Link to={`/book-edit/${book.id}`}>
                                            <Icon name='edit' size='big' style={{ marginBottom: '10px', marginTop: '10px', marginLeft: '5px' }} />
                                        </Link>
                                    } />
                                )}
                                <Popup content='Download PDF' trigger={
                                    <Icon name='download' size='big' style={{ marginBottom: '10px' }} onClick={() => handleBookContentDownload(book)} />
                                } />
                            </>
                        )}
                    </div>
                </Item.Image>
            </Item>
        ));
    }

    const filterOptions = [
        { key: 'all', text: 'All Books', value: 'all' },
        { key: 'purchased', text: 'Purchased Books', value: 'purchased' },
        { key: 'posted', text: 'Posted Books', value: 'posted' }
    ];

    return (
        <Container>
            <Segment loading={isBooksLoading} color='blue'>
                <Grid stackable divided>
                    <Grid.Row columns='4'>
                        <Grid.Column width='3'>
                            <Header as='h2'>
                                <Icon name='book' />
                                <Header.Content>{location.pathname === '/personal-library' ? 'My Books' : 'Books'}</Header.Content>
                            </Header>
                        </Grid.Column>
                        {user.role === 'AUTHOR' && location.pathname === '/library' && (
                            <Grid.Column width='3'>
                                <Button
                                    as={Link}
                                    to="/add-book-form"
                                    color='green'
                                    icon='add'
                                    content='Add New Book'
                                />
                            </Grid.Column>
                        )}
                        {user.role === 'AUTHOR' && location.pathname === '/personal-library' && (
                            <Grid.Column width='3'>
                                <Dropdown
                                    placeholder='Filter Books'
                                    fluid
                                    selection
                                    options={filterOptions}
                                    value={filter}
                                    onChange={(e, { value }) => setFilter(value)}
                                />
                            </Grid.Column>
                        )}
                    </Grid.Row>
                    <Grid.Row columns='4'>
                        <Grid.Column width='13'>
                            <Form onSubmit={handleSearchBook}>
                                <Form.Group widths='equal'>
                                    <Form.Input
                                        fluid
                                        name='title'
                                        placeholder='Search by Title'
                                        value={searchParams.title}
                                        onChange={handleInputChange}
                                    />
                                    <Form.Input
                                        fluid
                                        name='author'
                                        placeholder='Search by Author'
                                        value={searchParams.author}
                                        onChange={handleInputChange}
                                    />
                                    <Form.Input
                                        fluid
                                        name='genre'
                                        placeholder='Search by Genre'
                                        value={searchParams.genre}
                                        onChange={handleInputChange}
                                    />
                                    <Form.Input
                                        fluid
                                        name='minPrice'
                                        placeholder='Min Price'
                                        type='number'
                                        value={searchParams.minPrice}
                                        onChange={handleInputChange}
                                    />
                                    <Form.Input
                                        fluid
                                        name='maxPrice'
                                        placeholder='Max Price'
                                        type='number'
                                        value={searchParams.maxPrice}
                                        onChange={handleInputChange}
                                    />
                                    <Form.Button icon='search' />
                                </Form.Group>
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
