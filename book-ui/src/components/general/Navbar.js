import React from 'react'
import { Link } from 'react-router-dom'
import { Container, Menu } from 'semantic-ui-react'
import { useAuth } from '../context/AuthContext'

function Navbar() {
  const { getUser, userIsAuthenticated, userLogout } = useAuth()

  const logout = () => {
    userLogout()
  }

  const unauthorizedStyle = () => {
    return userIsAuthenticated() ? { "display": "none" } : { "display": "block" }
  }

  const authorizedStyle = () => {
    return userIsAuthenticated() ? { "display": "block" } : { "display": "none" }
  }

  const adminNavbarStyle = () => {
    const user = getUser()
    return user && user.role === 'ADMIN' ? { "display": "block" } : { "display": "none" }
  }

  const adminAuthorNavbarStyle = () => {
    const user = getUser()
    return user && ['ADMIN', 'AUTHOR'].includes(user.role) ? { "display": "block" } : { "display": "none" }
  }

  const authorUserStyle = () => {
    const user = getUser()
    return user && ['USER', 'AUTHOR'].includes(user.role) ? { "display": "block" } : { "display": "none" }
  }

  const getUserName = () => {
    const user = getUser()
    return user ? user.name : ''
  }

  return (
      <Menu inverted color='brown' stackable size='massive' style={{borderRadius: 0}}>
        <Container>
          <Menu.Item header>Book-UI</Menu.Item>
          <Menu.Item as={Link} exact='true' to="/">Home</Menu.Item>
          <Menu.Item as={Link} to="/adminpage" style={adminNavbarStyle()}>AdminPage</Menu.Item>
          <Menu.Item as={Link} to="/library" style={authorUserStyle()}>Library</Menu.Item>
          <Menu.Item as={Link} to="/personal-library" style={authorUserStyle()}>My Books</Menu.Item>
          <Menu.Item as={Link} to="/purchase-requests" style={adminAuthorNavbarStyle()}>Purchase Requests</Menu.Item>
          <Menu.Item as={Link} to="/author-requests" style={adminNavbarStyle()}>Author Requests</Menu.Item>
          <Menu.Menu position='right'>
            <Menu.Item as={Link} to="/login" style={unauthorizedStyle()}>Login</Menu.Item>
            <Menu.Item as={Link} to="/signup" style={unauthorizedStyle()}>Sign Up</Menu.Item>
            <Menu.Item as={Link} to="/profile" style={authorizedStyle()} icon='user' />
            <Menu.Item header style={authorizedStyle()}>{`Hi ${getUserName()}`}</Menu.Item>
            <Menu.Item as={Link} to="/" style={authorizedStyle()} onClick={logout}>Logout</Menu.Item>
          </Menu.Menu>
        </Container>
      </Menu>
  )
}

export default Navbar
