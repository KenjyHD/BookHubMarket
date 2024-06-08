import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import {Button, Container, Divider, Grid, Header, Image, Loader, Message, Modal, Segment} from 'semantic-ui-react';
import {bookApi} from "./BookApi";
import {useAuth} from "../context/AuthContext";

function BookDetails() {
    const { id } = useParams();
    const user = useAuth().getUser()
    const isAdmin = user.role === 'ADMIN';
    const [book, setBook] = useState(null);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);
    const [status, setStatus] = useState({
        isOwned: false,
        isRequested: false,
        requestStatus: null
    });
    const [message, setMessage] = useState('');
    const [modalOpen, setModalOpen] = useState(false);
    const [authorRequest, setAuthorRequest] = useState(null);

    useEffect(() => {
        const fetchBookDetails = async () => {
            try {
                const bookResponse = await bookApi.getBook(user, id)
                if (!bookResponse) {
                    setError('Failed to fetch book details');
                    return;
                }

                setBook(bookResponse.data);
                const statusResponse = await bookApi.checkPurchaseRequest(user, id);
                setStatus(statusResponse.data);

                if (isAdmin) {
                    const authorRequestResponse = await bookApi.getPendingAuthorRequest(user, id);
                    setAuthorRequest(authorRequestResponse.data);
                }
            } catch (error) {
                console.error('Error fetching book details or status:', error);
                setError('Failed to fetch book details. Please try again later.');
            } finally {
                setIsLoading(false);
            }
        };

        fetchBookDetails();
    }, [id, isAdmin]);

    const handleDownload = async () => {
        try {
            const response = await bookApi.downloadBook(user, id);
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', `book_${id}.pdf`);
            document.body.appendChild(link);
            link.click();
            link.parentNode.removeChild(link);
        } catch (error) {
            console.error('Error downloading book:', error);
            setError('Failed to download the book. Please try again later.');
        }
    };

    const handlePurchaseRequest = async () => {
        try {
            const response = await bookApi.createPurchaseRequest(user, id);
            if (response.status === 200) {
                setMessage('Purchase request sent successfully!');
            } else if (response.status === 409) {
                const errorMessage = response.data || 'Failed to create purchase request. Please try again.';
                setMessage(errorMessage.toString());
            } else {
                setMessage('Something went wrong. Please try again.');
            }
        } catch (error) {
            console.error('Error creating purchase request:', error);
            setMessage('Failed to create purchase request. Please try again.');
        } finally {
            setModalOpen(true);
        }
    };

    const handleModalClose = () => {
        setModalOpen(false);
        window.location.reload();
    };

    const handleApproveRequest = async () => {
        try {
            await bookApi.approveAuthorRequest(user, authorRequest.id);
            setModalOpen(true);
            setMessage('Author request approved successfully.');
        } catch (error) {
            console.error('Error approving author request:', error);
            setMessage('Failed to approve author request. Please try again later.');
            setModalOpen(true);
        }
    };

    const handleRejectRequest = async () => {
        try {
            await bookApi.rejectAuthorRequest(user, authorRequest.id);
            setMessage('Author request rejected successfully.');
            setModalOpen(true);
        } catch (error) {
            console.error('Error rejecting author request:', error);
            setMessage('Failed to reject author request. Please try again later.');
            setModalOpen(true);
        }
    };


    if (isLoading) {
        return (
            <Container textAlign='center'>
                <Loader active inline='centered'>Loading...</Loader>
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

    const isBookAuthor = user.role === 'AUTHOR' && book.authorId === user.id;

    return (
        <Container>
            <Segment>
                <Header as='h2' textAlign='center'>{book.title}</Header>
                <Grid>
                    <Grid.Row>
                        <Grid.Column width={6}>
                            <Image
                                src={`http://localhost:8080/file-storage/book-covers/${book.coverId}`}
                                size='medium'
                                bordered
                                rounded
                                centered
                            />
                        </Grid.Column>
                        <Grid.Column width={10}>
                            <p><strong>Author:</strong> {book.author}</p>
                            <p><strong>Price:</strong> ${book.price.toFixed(2)}</p>
                            <p><strong>Genre:</strong> {book.genre || 'No genre specified'}</p>
                            <p><strong>Description:</strong> {book.description || 'No description available'}</p>
                        </Grid.Column>
                    </Grid.Row>
                </Grid>
                <Divider/>

                {isAdmin && !!authorRequest && (
                    <Segment textAlign='center'>
                        <Button color='green' onClick={handleApproveRequest} style={{ marginRight: '10px' }}>
                            Approve Author Request
                        </Button>
                        <Button color='red' onClick={handleRejectRequest}>
                            Reject Author Request
                        </Button>
                    </Segment>
                )}

                {status.isOwned || isBookAuthor ? (
                    <Button primary onClick={handleDownload}>Download PDF</Button>
                ) : (
                    status.isRequested ? (
                        <Message positive>
                            <Message.Header>Purchase Request Exists</Message.Header>
                            <p>You have already requested this book for purchase. Please wait for decision.</p>
                        </Message>
                    ) : (
                        <Button primary onClick={handlePurchaseRequest}>Request Purchase</Button>
                    )
                )}

                <Modal
                    open={modalOpen}
                    onClose={handleModalClose}
                    closeOnDimmerClick={!message}
                >
                    <Modal.Header>{message.includes('successfully') ? 'Success' : 'Error'}</Modal.Header>
                    <Modal.Content>
                        <p>{message}</p>
                    </Modal.Content>
                    <Modal.Actions>
                        <Button primary onClick={handleModalClose}>Close</Button>
                    </Modal.Actions>
                </Modal>
            </Segment>
        </Container>
    );
}

export default BookDetails;
