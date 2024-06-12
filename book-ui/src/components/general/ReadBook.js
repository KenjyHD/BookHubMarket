import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { bookApi } from './BookApi';
import { useAuth } from '../context/AuthContext';
import { Container, Loader, Message } from 'semantic-ui-react';
import PdfViewer from "../../PdfViewer";

function ReadBook() {
    const { id } = useParams();
    const user = useAuth().getUser();
    const [fileUrl, setFileUrl] = useState('');
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchBookUrl = async () => {
            try {
                const response = await bookApi.downloadBook(user, id);
                const url = window.URL.createObjectURL(new Blob([response.data]));
                setFileUrl(url);
            } catch (error) {
                console.error('Error fetching book URL:', error);
                setError('Failed to fetch book URL. Please try again later.');
            } finally {
                setIsLoading(false);
            }
        };

        fetchBookUrl();
    }, [id]);

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
        <PdfViewer fileUrl={fileUrl} />
    );
}

export default ReadBook;
