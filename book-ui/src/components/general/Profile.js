import React, { useState, useEffect } from 'react';
import {Button, Container, Divider, Form, Header, Modal, Segment} from 'semantic-ui-react';
import { useAuth } from '../context/AuthContext';
import { bookApi } from './BookApi';
import { handleLogError } from './Helpers';
import './Profile.css';

function Profile() {
    const Auth = useAuth();
    const user = Auth.getUser();

    const [formData, setFormData] = useState({
        username: '',
        name: '',
        email: '',
        password: '',
        confirmPassword: ''
    });
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');
    const [modalOpen, setModalOpen] = useState(false);

    useEffect(() => {
        if (user) {
            fetchUserDetails();
        }
    }, []);

    const fetchUserDetails = async () => {
        try {
            const response = await bookApi.getUserDetails(user);
            const userDetails = response.data;
            setFormData({
                username: userDetails.username,
                name: userDetails.name,
                email: userDetails.email,
                password: '',
                confirmPassword: ''
            });
        } catch (error) {
            handleLogError(error);
            setError('Failed to fetch user details.');
            setModalOpen(true);
        }
    };

    const handleInputChange = (e, { name, value }) => {
        setFormData(prevState => ({ ...prevState, [name]: value }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const { username, name, email, password, confirmPassword } = formData;

        if (password !== confirmPassword) {
            setError('Passwords do not match');
            setModalOpen(true)
            return;
        }

        try {
            const updatedUser = { ...user, name, email, password };
            const response = await bookApi.updateUser(updatedUser);
            setSuccess('Profile updated successfully');
            if (password) {
                const authdata = window.btoa(username + ':' + password);
                const authenticatedUser = { id: user.id, name: response.data.name, role: user.role, authdata };
                Auth.setUserData(authenticatedUser);
            }
            setModalOpen(true);
        } catch (error) {
            handleLogError(error);
            setError('Failed to update profile. Please ensure the email is unique.');
            setModalOpen(true);
        }
    };

    const handleModalClose = () => {
        setModalOpen(false);
        if (success) {
            window.location.reload();
        }
    };

    return (
        <Container>
            <Header as='h2'>Profile</Header>
            <Divider />
            <Segment raised>
                <Form onSubmit={handleSubmit} error={!!error} success={!!success}>
                    <Form.Field>
                        <label>Username</label>
                        <p style={{ fontSize: '1.2em', color: 'black' }}>{formData.username}</p>
                    </Form.Field>
                    <Form.Input
                        label='Name'
                        name='name'
                        value={formData.name}
                        onChange={handleInputChange}
                        required
                    />
                    <Form.Input
                        label='Email'
                        name='email'
                        type='email'
                        value={formData.email}
                        onChange={handleInputChange}
                        required
                    />
                    <Form.Input
                        label='Password'
                        name='password'
                        type='password'
                        value={formData.password}
                        onChange={handleInputChange}
                    />
                    <Form.Input
                        label='Confirm Password'
                        name='confirmPassword'
                        type='password'
                        value={formData.confirmPassword}
                        onChange={handleInputChange}
                    />
                    <Button type='submit' color='blue'>Update</Button>
                </Form>
            </Segment>
            <Modal open={modalOpen} onClose={handleModalClose}>
                <Modal.Header>{success ? 'Success' : 'Error'}</Modal.Header>
                <Modal.Content>
                    <p>{success || error}</p>
                </Modal.Content>
                <Modal.Actions>
                    <Button onClick={handleModalClose} color='blue'>OK</Button>
                </Modal.Actions>
            </Modal>
        </Container>
    );
}

export default Profile;
