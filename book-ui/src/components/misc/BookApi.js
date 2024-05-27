import axios from 'axios'
import { config } from '../../Constants'

export const bookApi = {
  authenticate,
  signup,
  numberOfUsers,
  numberOfBooks,
  getUsers,
  deleteUser,
  getBooks,
  getBook,
  deleteBook,
  addBook,
  createPurchaseRequest,
  checkPurchaseRequest
}

function authenticate(username, password) {
  return instance.post('/auth/authenticate', { username, password }, {
    headers: { 'Content-type': 'application/json' }
  })
}

function signup(user) {
  return instance.post('/auth/signup', user, {
    headers: { 'Content-type': 'application/json' }
  })
}

function numberOfUsers() {
  return instance.get('/public/numberOfUsers')
}

function numberOfBooks() {
  return instance.get('/public/numberOfBooks')
}

function getUsers(user, username) {
  const url = username ? `/api/users/${username}` : '/api/users'
  return instance.get(url, {
    headers: { 'Authorization': basicAuth(user) }
  })
}

function deleteUser(user, username) {
  return instance.delete(`/api/users/${username}`, {
    headers: { 'Authorization': basicAuth(user) }
  })
}

function getBooks(user, text) {
  const url = text ? `/api/books?text=${text}` : '/api/books'
  return instance.get(url, {
    headers: { 'Authorization': basicAuth(user) }
  })
}

function getBook(user, id, text) {
  const url = text ? `/api/books?text=${text}` : `/api/books/${id}`
  return instance.get(url, {
    headers: { 'Authorization': basicAuth(user) }
  })
}

function deleteBook(user, id) {
  return instance.delete(`/api/books/${id}`, {
    headers: { 'Authorization': basicAuth(user) }
  })
}

function addBook(user, book) {
  return instance.post('/api/books', book, {
    headers: {
      'Content-Type': 'multipart/form-data',
      'Authorization': basicAuth(user)
    }
  })
}

function createPurchaseRequest(user, bookId) {
  return instance.post('/api/purchase', {
    userId: user.id,
    bookId: bookId
  }, {
    headers: {
      'Content-Type': 'application/json',
      'Authorization': basicAuth(user)
    }
  }).then(response => response)
      .catch(error => {
        if (error.response) {
          return error.response;
        }
        throw error;
      });
}

function checkPurchaseRequest(user, bookId) {
  return instance.get(`/api/purchase/check?userId=${user.id}&bookId=${bookId}`, {
    headers: { 'Authorization': basicAuth(user) }
  });
}

// -- Axios

const instance = axios.create({
  baseURL: config.url.API_BASE_URL
})

// -- Helper functions

function basicAuth(user) {
  return `Basic ${user.authdata}`
}