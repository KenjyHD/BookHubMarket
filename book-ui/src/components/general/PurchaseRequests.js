    import React, { useEffect, useState } from 'react';
    import { Button, Container, Header, List, Segment, Divider } from 'semantic-ui-react';
    import { useAuth } from '../context/AuthContext';
    import { bookApi } from './BookApi';
    import { handleLogError } from './Helpers';

    function AdminPurchaseRequests() {
        const Auth = useAuth();
        const user = Auth.getUser();
        const isAdmin = user.role === 'ADMIN';
        const isAuthor = user.role === 'AUTHOR';

        const [purchaseRequests, setPurchaseRequests] = useState({
            pending: [],
            approved: [],
            rejected: []
        });

        useEffect(() => {
            if (isAdmin || isAuthor) {
                fetchPurchaseRequests();
            }
        }, [isAdmin, isAuthor]);

        const fetchPurchaseRequests = async () => {
            try {
                let response;
                if (isAdmin) {
                    response = await bookApi.getPurchaseRequests(user);
                } else if (isAuthor) {
                    response = await bookApi.getPurchaseRequestsForAuthor(user);
                }

                const pending = response.data.filter(request => request.status === 'PENDING');
                const approved = response.data.filter(request => request.status === 'APPROVED');
                const rejected = response.data.filter(request => request.status === 'REJECTED');
                setPurchaseRequests({ pending, approved, rejected });
            } catch (error) {
                handleLogError(error);
            }
        };

        const handleAcceptRequest = async (requestId) => {
            try {
                await bookApi.approvePurchaseRequest(user, requestId);
                await fetchPurchaseRequests();
            } catch (error) {
                handleLogError(error);
            }
        };

        const handleRejectRequest = async (requestId) => {
            try {
                await bookApi.rejectPurchaseRequest(user, requestId);
                await fetchPurchaseRequests();
            } catch (error) {
                handleLogError(error);
            }
        };

        return (
            <Container>
                <Header as='h2'>Purchase Requests</Header>
                <Segment>
                    <Header as='h3' color='blue'>Pending Requests</Header>
                    <List divided relaxed>
                        {purchaseRequests.pending.length === 0 ? (
                            <List.Item>No pending requests</List.Item>
                        ) : (
                            purchaseRequests.pending.map(request => (
                                <List.Item key={request.id}>
                                    <List.Content>
                                        <List.Header>{request.username} wants to purchase "{request.bookTitle}"</List.Header>
                                        <List.Description>
                                            <Button color='green' onClick={() => handleAcceptRequest(request.id)}>Accept</Button>
                                            <Button color='red' onClick={() => handleRejectRequest(request.id)}>Reject</Button>
                                        </List.Description>
                                    </List.Content>
                                </List.Item>
                            ))
                        )}
                    </List>
                    <Divider />
                    <Header as='h3' color='green'>Approved Requests</Header>
                    <List divided relaxed>
                        {purchaseRequests.approved.length === 0 ? (
                            <List.Item>No approved requests</List.Item>
                        ) : (
                            purchaseRequests.approved.map(request => (
                                <List.Item key={request.id}>
                                    <List.Content>
                                        <List.Header>{request.username} purchased "{request.bookTitle}"</List.Header>
                                    </List.Content>
                                </List.Item>
                            ))
                        )}
                    </List>
                    <Divider />
                    <Header as='h3' color='red'>Rejected Requests</Header>
                    <List divided relaxed>
                        {purchaseRequests.rejected.length === 0 ? (
                            <List.Item>No rejected requests</List.Item>
                        ) : (
                            purchaseRequests.rejected.map(request => (
                                <List.Item key={request.id}>
                                    <List.Content>
                                        <List.Header>{request.username} was rejected to purchase "{request.bookTitle}"</List.Header>
                                    </List.Content>
                                </List.Item>
                            ))
                        )}
                    </List>
                </Segment>
            </Container>
        );
    }

    export default AdminPurchaseRequests;
