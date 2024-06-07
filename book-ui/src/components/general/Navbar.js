import React from 'react'
import { Link } from 'react-router-dom'
import { Container, Menu } from 'semantic-ui-react'
import { useAuth } from '../context/AuthContext'

function Navbar() {
  const { getUser, userIsAuthenticated, userLogout } = useAuth()

  const logout = () => {
    userLogout()
  }

  const enterMenuStyle = () => {
    return userIsAuthenticated() ? { "display": "none" } : { "display": "block" }
  }

  const logoutMenuStyle = () => {
    return userIsAuthenticated() ? { "display": "block" } : { "display": "none" }
  }

  const adminPageStyle = () => {
    const user = getUser()
    return user && user.role === 'ADMIN' ? { "display": "block" } : { "display": "none" }
  }

  const purchaseRequestsPageStyle = () => {
    const user = getUser()
    return user && ['ADMIN', 'AUTHOR'].includes(user.role) ? { "display": "block" } : { "display": "none" }
  }

  const libraryStyle = () => {
    const user = getUser()
    return user && ['USER', 'AUTHOR'].includes(user.role) ? { "display": "block" } : { "display": "none" }
  }

  const personalLibraryStyle = () => {
    const user = getUser()
    return user && ['USER', 'AUTHOR'].includes(user.role) ? { "display": "block" } : { "display": "none" }
  }

  const getUserName = () => {
    const user = getUser()
    return user ? user.name : ''
  }

  return (
    <Menu inverted color='blue' stackable size='massive' style={{borderRadius: 0}}>
      <Container>
        <Menu.Item header>Book-UI</Menu.Item>
        <Menu.Item as={Link} exact='true' to="/">Home</Menu.Item>
        <Menu.Item as={Link} to="/adminpage" style={adminPageStyle()}>AdminPage</Menu.Item>
        <Menu.Item as={Link} to="/library" style={libraryStyle()}>Library</Menu.Item>
        <Menu.Item as={Link} to="/personal-library" style={personalLibraryStyle()}>My Books</Menu.Item>
        <Menu.Item as={Link} to="/purchase-requests" style={purchaseRequestsPageStyle()}>Purchase Requests</Menu.Item>
        <Menu.Menu position='right'>
          <Menu.Item as={Link} to="/login" style={enterMenuStyle()}>Login</Menu.Item>
          <Menu.Item as={Link} to="/signup" style={enterMenuStyle()}>Sign Up</Menu.Item>
          <Menu.Item as={Link} to="/profile" style={logoutMenuStyle()} icon='user' />
          <Menu.Item header style={logoutMenuStyle()}>{`Hi ${getUserName()}`}</Menu.Item>
          <Menu.Item as={Link} to="/" style={logoutMenuStyle()} onClick={logout}>Logout</Menu.Item>
        </Menu.Menu>
      </Container>
    </Menu>
  )
}

export default Navbar
