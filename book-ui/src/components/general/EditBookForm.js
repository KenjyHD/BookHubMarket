import React, {useEffect, useState} from 'react';
import {Button, Container, Form, Label, Message, Modal} from 'semantic-ui-react';
import {bookApi} from "./BookApi";
import {handleLogError} from "./Helpers";
import {useAuth} from "../context/AuthContext";
import {useNavigate, useParams} from "react-router-dom";
import { config } from '../../Constants'

function EditBookForm() {
    const {id} = useParams();
    const [title, setTitle] = useState('');
    const [author, setAuthor] = useState('');
    const [genre, setGenre] = useState('');
    const [description, setDescription] = useState('');
    const [price, setPrice] = useState('');
    const [bookContent, setBookContent] = useState(null);
    const [bookCover, setBookCover] = useState(null);
    const [bookCoverId, setBookCoverId] = useState(null);
    const [bookContentFilename, setBookContentFilename] = useState('');
    const [bookCoverFilename, setBookCoverFilename] = useState('');
    const [errors, setErrors] = useState({});
    const [hasPendingRequest, setHasPendingRequest] = useState(false);
    const [successModalOpen, setSuccessModalOpen] = useState(false);
    const [accessDeniedModalOpen, setAccessDeniedModalOpen] = useState(false);
    const [bookNotFound, setBookNotFound] = useState(false);

    const user = useAuth().getUser();
    const navigate = useNavigate();

    useEffect(() => {
        fetchBookDetails();
    }, []);

    const fetchBookDetails = async () => {
        try {
            const response = await bookApi.getBook(user, id);
            const book = response.data;

            if ((user.role !== 'ADMIN') && !(user.role === 'AUTHOR' && book.authorId === user.id)) {
                setAccessDeniedModalOpen(true);
                return;
            }

            setTitle(book.title);
            setAuthor(book.author);
            setGenre(book.genre);
            setDescription(book.description);
            setPrice(book.price);
            setBookCoverId(book.coverId);
            setBookContentFilename(book.contentFilename);
            setBookCoverFilename(book.coverFilename);
        } catch (error) {
            if (error.response.status === 404) {
                setBookNotFound(true);
                return;
            }
            handleLogError(error);
        }
    };

    const handleInputChange = (setter, fieldName) => (e) => {
        setter(e.target.value);
        if (errors[fieldName]) {
            setErrors((prevErrors) => ({ ...prevErrors, [fieldName]: null }));
        }
    };

    const handleFileChange = (setter, fieldName) => (event) => {
        const file = event.target.files[0];
        setter(file);
        if (errors[fieldName]) {
            setErrors((prevErrors) => ({ ...prevErrors, [fieldName]: null }));
        }
    };

    const validateFields = () => {
        const errors = {};
        if (!title) errors.title = 'Title is required';
        if (!author) errors.author = 'Author is required';
        if (!price) errors.price = 'Price is required';
        return errors;
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        const errors = validateFields();
        if (Object.keys(errors).length > 0) {
            setErrors(errors);
            return;
        }

        const formData = new FormData();
        const bookData = { title, author, genre, description, price };
        formData.append('book', new Blob([JSON.stringify(bookData)], { type: 'application/json' }));
        if (bookContent) {
            formData.append('bookContent', bookContent);
        }
        if (bookCover) {
            formData.append('bookCover', bookCover);
        }

        try {
            await bookApi.updateBook(user, formData, id);
            setSuccessModalOpen(true);
        } catch (error) {
            handleLogError(error);
        }
    };

    const handleBookContentDownload = async () => {
        try {
            const response = await bookApi.downloadBook(user, id);
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', bookContentFilename);
            document.body.appendChild(link);
            link.click();
            link.parentNode.removeChild(link);
        } catch (error) {
            console.error('Error downloading book:', error);
            setErrors('Failed to download the book. Please try again later.');
        }
    };

    const handleBookCoverDownload = async () => {
        try {
            const response = await fetch(`${config.url.API_BASE_URL}/file-storage/book-covers/${bookCoverId}`);
            if (!response.ok) {
                setErrors('Network response was not ok');
                return;
            }
            const blob = await response.blob();
            const url = window.URL.createObjectURL(blob);
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', bookCoverFilename);
            document.body.appendChild(link);
            link.click();
            link.parentNode.removeChild(link);
        } catch (error) {
            console.error('Error downloading book cover:', error);
            setErrors('Failed to download the book cover. Please try again later.');
        }
    };

    const handleCloseSuccessModal = () => {
        setSuccessModalOpen(false);
        navigate(`/book/${id}`);
    };

    const handleCloseAccessDeniedModal = () => {
        setAccessDeniedModalOpen(false);
        navigate(-1);
    };

    const handleCloseBookNotFoundModal = () => {
        setBookNotFound(false);
        navigate(-1);
    };

    return (
        <Container>
            {bookNotFound ? (
                <Modal open={bookNotFound} onClose={handleCloseBookNotFoundModal}>
                    <Modal.Header>Book Not Found</Modal.Header>
                    <Modal.Content>
                        <p>The book with the given ID was not found.</p>
                    </Modal.Content>
                    <Modal.Actions>
                        <Button onClick={handleCloseBookNotFoundModal}>OK</Button>
                    </Modal.Actions>
                </Modal>
            ) : accessDeniedModalOpen ? (
                <Modal open={accessDeniedModalOpen} onClose={handleCloseAccessDeniedModal}>
                    <Modal.Header>Access Denied</Modal.Header>
                    <Modal.Content>
                        <p>You do not have permission to edit this book.</p>
                    </Modal.Content>
                    <Modal.Actions>
                        <Button onClick={handleCloseAccessDeniedModal}>OK</Button>
                    </Modal.Actions>
                </Modal>
            ) : (
                <Form onSubmit={handleSubmit}>
                    <Form.Field error={!!errors.title}>
                        <label>Title <span style={{color: 'red'}}>*</span></label>
                        <input
                            type="text"
                            placeholder="Title"
                            value={title}
                            onChange={handleInputChange(setTitle, 'title')}
                        />
                        {errors.title && (
                            <Label basic color='red' pointing>
                                {errors.title}
                            </Label>
                        )}
                    </Form.Field>
                    <Form.Field error={!!errors.author}>
                        <label>Author <span style={{color: 'red'}}>*</span></label>
                        <input
                            type="text"
                            placeholder="Author"
                            value={author}
                            onChange={handleInputChange(setAuthor, 'author')}
                        />
                        {errors.author && (
                            <Label basic color='red' pointing>
                                {errors.author}
                            </Label>
                        )}
                    </Form.Field>
                    <Form.Field>
                        <label>Genre</label>
                        <input
                            type="text"
                            placeholder="Genre"
                            value={genre}
                            onChange={(e) => setGenre(e.target.value)}
                        />
                    </Form.Field>
                    <Form.Field>
                        <label>Description</label>
                        <textarea
                            placeholder="Description"
                            value={description}
                            onChange={(e) => setDescription(e.target.value)}
                        />
                    </Form.Field>
                    <Form.Field error={!!errors.price}>
                        <label>Price <span style={{color: 'red'}}>*</span></label>
                        <input
                            type="number"
                            placeholder="Price"
                            value={price}
                            onChange={handleInputChange(setPrice, 'price')}
                        />
                        {errors.price && (
                            <Label basic color='red' pointing>
                                {errors.price}
                            </Label>
                        )}
                    </Form.Field>
                    <Form.Field error={!!errors.bookContent}>
                        <label>Book PDF</label>
                        <input
                            type="file"
                            accept="application/pdf"
                            onChange={handleFileChange(setBookContent, 'bookContent')}
                        />
                        <Button onClick={handleBookContentDownload} type="button" style={{ marginTop: '10px' }}>Download Current PDF</Button>
                        {errors.bookContent && (
                            <Label basic color='red' pointing>
                                {errors.bookContent}
                            </Label>
                        )}
                    </Form.Field>
                    <Form.Field error={!!errors.bookCover}>
                        <label>Book cover</label>
                        <input
                            type="file"
                            accept="image/jpeg, image/png"
                            onChange={handleFileChange(setBookCover, 'bookCover')}
                        />
                        <Button onClick={handleBookCoverDownload} type="button" style={{ marginTop: '10px' }}>Download Current Cover</Button>
                        {errors.bookCover && (
                            <Label basic color='red' pointing>
                                {errors.bookCover}
                            </Label>
                        )}
                    </Form.Field>
                    <Button primary type="submit" disabled={hasPendingRequest}>Update</Button>
                </Form>
                )}
                <Modal open={successModalOpen} onClose={handleCloseSuccessModal}>
                    <Modal.Header>Book Updated Successfully</Modal.Header>
                    <Modal.Content>
                        <p>Your book has been updated successfully.</p>
                    </Modal.Content>
                    <Modal.Actions>
                        <Button onClick={handleCloseSuccessModal}>OK</Button>
                    </Modal.Actions>
                </Modal>
        </Container>
    );
}

export default EditBookForm;
