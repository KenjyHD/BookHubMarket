import React, { useState } from 'react'
import { NavLink, Navigate } from 'react-router-dom'
import { Button, Form, Grid, Segment, Message } from 'semantic-ui-react'
import { useAuth } from '../context/AuthContext'
import { bookApi } from '../general/BookApi'
import { handleLogError } from '../general/Helpers'
import { Buffer } from 'buffer'

function Login() {
  const Auth = useAuth()
  const isLoggedIn = Auth.userIsAuthenticated()

  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [isError, setIsError] = useState(false)

  const handleInputChange = (e, { name, value }) => {
    if (name === 'username') {
      setUsername(value)
    } else if (name === 'password') {
      setPassword(value)
    }
  }

  const handleSubmit = async (e) => {
    e.preventDefault()

    if (!(username && password)) {
      setIsError(true)
      return
    }

    try {
      const response = await bookApi.authenticate(username, password)
      const { id, name, role } = response.data
      const authdata = Buffer.from(`${username}:${password}`, 'utf-8').toString('base64')
      const authenticatedUser = { id, name, role, authdata }

      Auth.setUserData(authenticatedUser)

      setUsername('')
      setPassword('')
      setIsError(false)
    } catch (error) {
      handleLogError(error)
      setIsError(true)
    }
  }

  if (isLoggedIn) {
    return <Navigate to={'/'} />
  }

  return (
      <Grid textAlign='center'>
        <Grid.Column style={{ maxWidth: 450 }}>
          <Form size='large' onSubmit={handleSubmit}>
            <Segment>
              <Form.Input
                  fluid
                  autoFocus
                  name='username'
                  icon='user'
                  iconPosition='left'
                  placeholder='Username'
                  value={username}
                  onChange={handleInputChange}
              />
              <Form.Input
                  fluid
                  name='password'
                  icon='lock'
                  iconPosition='left'
                  placeholder='Password'
                  type='password'
                  value={password}
                  onChange={handleInputChange}
              />
              <Button color='blue' fluid size='large'>Login</Button>
            </Segment>
          </Form>
          <Message>{`Don't have already an account? `}
            <NavLink to="/signup" as={NavLink} color='teal'>Sign Up</NavLink>
          </Message>
          {isError && <Message negative>The username or password provided are incorrect!</Message>}
        </Grid.Column>
      </Grid>
  )
}

export default Login