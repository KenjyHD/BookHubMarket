import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import {Button, Container, Divider, Header, Image, Loader, Message, Modal, Segment} from 'semantic-ui-react';
import {bookApi} from "../misc/BookApi";
import {useAuth} from "../context/AuthContext";

function BookDetails() {
    const { id } = useParams();
    const user = useAuth().getUser()
    const [book, setBook] = useState(null);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);
    const [purchaseMessage, setPurchaseMessage] = useState(null);
    const [purchaseRequestExists, setPurchaseRequestExists] = useState(false);
    const [modalOpen, setModalOpen] = useState(false);

    useEffect(() => {
        const fetchBookDetails = async () => {
            try {
                const response = await bookApi.getBook(user, id)
                if (!response) {
                    setError('Failed to fetch book details');
                    return;
                }

                setBook(response.data);
                const purchaseRequestResponse = await bookApi.checkPurchaseRequest(user, id);
                setPurchaseRequestExists(purchaseRequestResponse.data.exists);
            } catch (error) {
                console.error('Error fetching book details:', error);
                setError('Failed to fetch book details. Please try again later.');
            } finally {
                setIsLoading(false);
            }
        };

        fetchBookDetails();
    }, [id]);

    const handlePurchaseRequest = async () => {
        try {
            const response = await bookApi.createPurchaseRequest(user, id);
            if (response.status === 200) {
                setPurchaseMessage('Purchase request sent successfully!');
                setModalOpen(true);
            } else if (response.status === 409) {
                const errorMessage = response.data || 'Failed to create purchase request. Please try again.';
                setPurchaseMessage(errorMessage.toString());
                setModalOpen(true);
            }
        } catch (error) {
            console.error('Error creating purchase request:', error);
            setPurchaseMessage('Failed to create purchase request. Please try again.');
            setModalOpen(true);
        }
    };

    const handleModalClose = () => {
        setModalOpen(false);
        window.location.reload();
    };


    if (isLoading) {
        return (
            <Container textAlign='center'>
                <Loader active inline='centered'>Loading...</Loader>
            </Container>
        );
    }

    if (!book) {
        return <Container>No book found</Container>;
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

    if (!book) {
        return (
            <Container textAlign='center'>
                <Message>
                    <Message.Header>No Book Found</Message.Header>
                    <p>The requested book could not be found.</p>
                </Message>
            </Container>
        );
    }

    return (
        <Container>
            <Segment>
                <Header as='h2' textAlign='center'>{book.title}</Header>
                <Image
                    src={`http://covers.openlibrary.org/b/id/${book.id}-L.jpg`}
                    size='medium'
                    bordered
                    rounded
                    centered
                />
                <Divider />
                <p><strong>Author:</strong> {book.author}</p>
                <p><strong>Price:</strong> ${book.price.toFixed(2)}</p>
                <p><strong>Genre:</strong> {book.genre || 'No genre specified'}</p>
                <p><strong>Description:</strong> {book.description || 'No description available'}</p>
                {!purchaseRequestExists && (
                    <Button primary onClick={handlePurchaseRequest}>Request Purchase</Button>
                )}
                {purchaseRequestExists && (
                    <Message positive>
                        <Message.Header>Purchase Request Exists</Message.Header>
                        <p>You have already requested this book for purchase.</p>
                    </Message>
                )}
                <Modal
                    open={modalOpen}
                    onClose={handleModalClose}
                    closeOnDimmerClick={!purchaseRequestExists}
                >
                    <Modal.Header>Success</Modal.Header>
                    {!purchaseRequestExists && (
                        <Modal.Content>
                            <p>{purchaseMessage}</p>
                        </Modal.Content>
                    )}
                    <Modal.Actions>
                        <Button primary onClick={handleModalClose}>Close</Button>
                    </Modal.Actions>
                </Modal>
            </Segment>
        </Container>
    );
}

export default BookDetails;
