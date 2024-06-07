import React, { useEffect, useState } from 'react';
import { Navigate, Link } from 'react-router-dom';
import { Container, Tab, Button, Form, Grid, Image, Input, Table } from 'semantic-ui-react';
import { useAuth } from '../context/AuthContext';
import { bookApi } from '../general/BookApi';
import { handleLogError } from '../general/Helpers';

function AdminPage() {
  const Auth = useAuth();
  const user = Auth.getUser();
  const isAdmin = user.role === 'ADMIN';

  const [users, setUsers] = useState([]);
  const [userUsernameSearch, setUserUsernameSearch] = useState('');
  const [isUsersLoading, setIsUsersLoading] = useState(false);

  const [books, setBooks] = useState([]);
  const [bookTextSearch, setBookTextSearch] = useState('');
  const [isBooksLoading, setIsBooksLoading] = useState(false);

  useEffect(() => {
    handleGetUsers();
    handleGetBooks();
  }, []);

  const handleInputChange = (e, { name, value }) => {
    if (name === 'userUsernameSearch') {
      setUserUsernameSearch(value);
    } else if (name === 'bookTextSearch') {
      setBookTextSearch(value);
    }
  };

  const handleGetUsers = async () => {
    try {
      setIsUsersLoading(true);
      const response = await bookApi.getUsers(user);
      setUsers(response.data);
    } catch (error) {
      handleLogError(error);
    } finally {
      setIsUsersLoading(false);
    }
  };

  const handleDeleteUser = async (username) => {
    try {
      await bookApi.deleteUser(user, username);
      await handleGetUsers();
    } catch (error) {
      handleLogError(error);
    }
  };

  const handleSearchUser = async () => {
    try {
      const response = await bookApi.getUsers(user, userUsernameSearch);
      setUsers(response.data);
    } catch (error) {
      handleLogError(error);
      setUsers([]);
    }
  };

  const handleGetBooks = async () => {
    try {
      setIsBooksLoading(true);
      const response = await bookApi.getBooks(user);
      setBooks(response.data);
    } catch (error) {
      handleLogError(error);
    } finally {
      setIsBooksLoading(false);
    }
  };

  const handleDeleteBook = async (id) => {
    try {
      await bookApi.deleteBook(user, id);
      await handleGetBooks();
    } catch (error) {
      handleLogError(error);
    }
  };

  const handleSearchBook = async () => {
    try {
      const response = await bookApi.getBooks(user, bookTextSearch);
      setBooks(response.data);
    } catch (error) {
      handleLogError(error);
      setBooks([]);
    }
  };

  if (!isAdmin) {
    return <Navigate to='/' />;
  }

  const panes = [
    {
      menuItem: { key: 'users', icon: 'users', content: 'Users' },
      render: () => (
          <Tab.Pane loading={isUsersLoading}>
            <Form onSubmit={handleSearchUser}>
              <Input
                  action={{ icon: 'search' }}
                  name='userUsernameSearch'
                  placeholder='Search by Username'
                  value={userUsernameSearch}
                  onChange={handleInputChange}
              />
            </Form>
            <Table compact striped selectable>
              <Table.Header>
                <Table.Row>
                  <Table.HeaderCell width={1} />
                  <Table.HeaderCell width={1}>ID</Table.HeaderCell>
                  <Table.HeaderCell width={3}>Username</Table.HeaderCell>
                  <Table.HeaderCell width={4}>Name</Table.HeaderCell>
                  <Table.HeaderCell width={5}>Email</Table.HeaderCell>
                  <Table.HeaderCell width={2}>Role</Table.HeaderCell>
                </Table.Row>
              </Table.Header>
              <Table.Body>
                {users.length === 0 ? (
                    <Table.Row key='no-user'>
                      <Table.Cell collapsing textAlign='center' colSpan='6'>No user</Table.Cell>
                    </Table.Row>
                ) : (
                    users.map(user => (
                        <Table.Row key={user.id}>
                          <Table.Cell collapsing>
                            <Button
                                circular
                                color='red'
                                size='small'
                                icon='trash'
                                disabled={user.username === 'admin'}
                                onClick={() => handleDeleteUser(user.username)}
                            />
                          </Table.Cell>
                          <Table.Cell>{user.id}</Table.Cell>
                          <Table.Cell>{user.username}</Table.Cell>
                          <Table.Cell>{user.name}</Table.Cell>
                          <Table.Cell>{user.email}</Table.Cell>
                          <Table.Cell>{user.role}</Table.Cell>
                        </Table.Row>
                    ))
                )}
              </Table.Body>
            </Table>
          </Tab.Pane>
      )
    },
    {
      menuItem: { key: 'books', icon: 'book', content: 'Books' },
      render: () => (
          <Tab.Pane loading={isBooksLoading}>
            <Grid stackable divided>
              <Grid.Row columns='2'>
                <Grid.Column width='5'>
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
                <Grid.Column>
                  <Container>
                    <Button as={Link} to="/add-book-form" primary>
                      Add Book
                    </Button>
                  </Container>
                </Grid.Column>
              </Grid.Row>
            </Grid>
            <Table compact striped selectable>
              <Table.Header>
                <Table.Row>
                  <Table.HeaderCell width={1} />
                  <Table.HeaderCell width={3}>Cover</Table.HeaderCell>
                  <Table.HeaderCell width={4}>Author</Table.HeaderCell>
                  <Table.HeaderCell width={8}>Title</Table.HeaderCell>
                </Table.Row>
              </Table.Header>
              <Table.Body>
                {books.length === 0 ? (
                    <Table.Row key='no-book'>
                      <Table.Cell collapsing textAlign='center' colSpan='4'>No book</Table.Cell>
                    </Table.Row>
                ) : (
                    books.map(book => (
                        <Table.Row key={book.id}>
                          <Table.Cell collapsing>
                            <Button
                                circular
                                color='red'
                                size='small'
                                icon='trash'
                                onClick={() => handleDeleteBook(book.id)}
                            />
                            <Button
                                circular
                                color='blue'
                                size='small'
                                icon='eye'
                                as={Link}
                                to={`/book/${book.id}`}
                            />
                          </Table.Cell>
                          <Table.Cell>
                            <Image src={`http://localhost:8080/file-storage/book-covers/${book.coverId}`} size='tiny' bordered rounded />
                          </Table.Cell>
                          <Table.Cell>{book.author}</Table.Cell>
                          <Table.Cell>{book.title}</Table.Cell>
                        </Table.Row>
                    ))
                )}
              </Table.Body>
            </Table>
          </Tab.Pane>
      )
    }
  ];

  return (
      <Container>
        <Tab menu={{ attached: 'top' }} panes={panes} />
      </Container>
  );
}

export default AdminPage;
