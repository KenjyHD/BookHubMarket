import React, { useState } from 'react';
import {Button, Container, Form, Label} from 'semantic-ui-react';
import {bookApi} from "../misc/BookApi";
import {handleLogError} from "../misc/Helpers";
import {useAuth} from "../context/AuthContext";

function AddBookForm() {
    const [title, setTitle] = useState('');
    const [author, setAuthor] = useState('');
    const [genre, setGenre] = useState('');
    const [description, setDescription] = useState('');
    const [price, setPrice] = useState('');
    const [pdfFile, setPdfFile] = useState(null);
    const [errors, setErrors] = useState({});

    const Auth = useAuth();
    const user = Auth.getUser();

    const handleInputChange = (setter, fieldName) => (e) => {
        setter(e.target.value);
        if (errors[fieldName]) {
            setErrors((prevErrors) => ({ ...prevErrors, [fieldName]: null }));
        }
    };

    const handleFileChange = (event) => {
        const file = event.target.files[0];
        setPdfFile(file);
        if (errors.pdfFile) {
            setErrors((prevErrors) => ({ ...prevErrors, pdfFile: null }));
        }
    };

    const validateFields = () => {
        const errors = {};
        if (!title) errors.title = 'Title is required';
        if (!author) errors.author = 'Author is required';
        if (!price) errors.price = 'Price is required';
        if (!pdfFile) errors.pdfFile = 'PDF file is required';
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
        formData.append('pdfFile', pdfFile);

        try {
            const response = await bookApi.addBook(user, formData);
            console.log('Book added successfully:', response.data);
            clearForm();
        } catch (error) {
            handleLogError(error)
        }
    };

    const clearForm = () => {
        setTitle('');
        setAuthor('');
        setGenre('');
        setDescription('');
        setPrice('');
        setPdfFile(null);
        setErrors({});
    };

    return (
        <Container>
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
                <Form.Field error={!!errors.pdfFile}>
                    <label>PDF File <span style={{color: 'red'}}>*</span></label>
                    <input
                        type="file"
                        accept=".pdf"
                        onChange={handleFileChange}
                    />
                    {errors.pdfFile && (
                        <Label basic color='red' pointing>
                            {errors.pdfFile}
                        </Label>
                    )}
                </Form.Field>
                <Button type="submit">Submit</Button>
            </Form>
        </Container>
    );
}

export default AddBookForm;
