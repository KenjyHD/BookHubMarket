import React from 'react'
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom'
import { AuthProvider } from './components/context/AuthContext'
import PrivateRoute from './components/general/PrivateRoute'
import Navbar from './components/general/Navbar'
import Home from './components/home/Home'
import Login from './components/home/Login'
import Signup from './components/home/Signup'
import AdminPage from './components/admin/AdminPage'
import Library from './components/user/Library'
import BookDetails from './components/general/BookDetails'
import AddBookForm from "./components/general/AddBookForm";
import AdminPurchaseRequests from "./components/general/PurchaseRequests";
import Profile from "./components/general/Profile";

function App() {
  return (
    <AuthProvider>
      <Router>
        <Navbar />
        <Routes>
          <Route path='/' element={<Home />} />
          <Route path='/login' element={<Login />} />
          <Route path='/signup' element={<Signup />} />
          <Route path="/adminpage" element={<PrivateRoute><AdminPage /></PrivateRoute>} />
          <Route path="/library" element={<PrivateRoute><Library /></PrivateRoute>} />
          <Route path="/personal-library" element={<PrivateRoute><Library /></PrivateRoute>} />
          <Route path="/book/:id" element={<PrivateRoute><BookDetails /></PrivateRoute>} />
          <Route path="/add-book-form" element={<PrivateRoute><AddBookForm /></PrivateRoute>} />
          <Route path="/purchase-requests" element={<PrivateRoute><AdminPurchaseRequests /></PrivateRoute>} />
          <Route path="/profile" element={<PrivateRoute><Profile /></PrivateRoute>} />
          <Route path="*" element={<Navigate to="/" />} />
        </Routes>
      </Router>
    </AuthProvider>
  )
}

export default App
