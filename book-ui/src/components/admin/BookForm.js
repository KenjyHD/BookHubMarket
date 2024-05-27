import React from 'react'
import { Button, Container } from 'semantic-ui-react'
import { Link } from 'react-router-dom';

function BookForm() {
    return (
      <Container>
        <Button as={Link} to="/add-book-form" primary>
          Add Book
        </Button>
      </Container>
  )
}

export default BookForm