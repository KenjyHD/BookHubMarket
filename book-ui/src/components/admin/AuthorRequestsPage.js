import React, { useEffect, useState } from 'react';
import {Button, Container, Table, Tab, Loader} from 'semantic-ui-react';
import { format } from 'date-fns';
import { Link } from 'react-router-dom';
import { bookApi } from '../general/BookApi.js';
import { useAuth } from "../context/AuthContext";

function AdminAuthorRequestsPage() {
    const user = useAuth().getUser();
    const [authorRequests, setAuthorRequests] = useState([]);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        fetchAuthorRequests();
    }, []);

    const fetchAuthorRequests = async () => {
        try {
            setIsLoading(true);
            const response = await bookApi.getAllAuthorRequests(user);
            setAuthorRequests(response.data);
        } catch (error) {
            console.error("Error fetching author requests:", error);
        } finally {
            setIsLoading(false);
        }
    };

    const renderAuthorRequests = (status) => {
        const filteredRequests = authorRequests.filter(request => request.status === status);
        return (
            <Table compact striped selectable>
                <Table.Header>
                    <Table.Row>
                        <Table.HeaderCell>Username</Table.HeaderCell>
                        {status !== 'REJECTED' && <Table.HeaderCell>Title</Table.HeaderCell>}
                        <Table.HeaderCell>Request Date</Table.HeaderCell>
                        <Table.HeaderCell>Status</Table.HeaderCell>
                        {status === 'PENDING' && <Table.HeaderCell>Actions</Table.HeaderCell>}
                    </Table.Row>
                </Table.Header>
                <Table.Body>
                    {filteredRequests.map(request => (
                        <Table.Row key={request.id}>
                            <Table.Cell>{request.username}</Table.Cell>
                            {status !== 'REJECTED' && <Table.Cell>{request.title}</Table.Cell>}
                            <Table.Cell>{format(new Date(request.requestDate), 'yyyy-MM-dd HH:mm')}</Table.Cell>
                            <Table.Cell>{request.status}</Table.Cell>
                            {status === 'PENDING' && (
                                <Table.Cell>
                                    <Button
                                        circular
                                        color='blue'
                                        size='small'
                                        icon='eye'
                                        as={Link}
                                        to={`/book/${request.bookId}`}
                                    />
                                </Table.Cell>
                            )}
                        </Table.Row>
                    ))}
                </Table.Body>
            </Table>
        );
    };

    const panes = [
        { menuItem: 'Pending', render: () => <Tab.Pane>{renderAuthorRequests('PENDING')}</Tab.Pane> },
        { menuItem: 'Approved', render: () => <Tab.Pane>{renderAuthorRequests('APPROVED')}</Tab.Pane> },
        { menuItem: 'Rejected', render: () => <Tab.Pane>{renderAuthorRequests('REJECTED')}</Tab.Pane> },
    ];

    return (
        <Container>
            {isLoading ? (
                <Loader active inline="centered" />
            ) : (
                <Tab panes={panes} />
            )}
        </Container>
    );
}

export default AdminAuthorRequestsPage;
