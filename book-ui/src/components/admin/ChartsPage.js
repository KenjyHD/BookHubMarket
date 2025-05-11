import React, { useEffect, useState } from 'react';
import { Navigate } from 'react-router-dom';
import { Container, Tab } from 'semantic-ui-react';
import { Bar, Pie } from 'react-chartjs-2';
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, ArcElement, Title, Tooltip, Legend } from 'chart.js';
import { useAuth } from '../context/AuthContext';
import { bookApi } from '../general/BookApi';
import { handleLogError } from '../general/Helpers';

ChartJS.register(CategoryScale, LinearScale, BarElement, ArcElement, Title, Tooltip, Legend);

function ChartsPage() {
  const Auth = useAuth();
  const user = Auth.getUser();
  const isAdmin = user.role === 'ADMIN';

  const [books, setBooks] = useState([]);
  const [users, setUsers] = useState([]);
  const [purchaseRequests, setPurchaseRequests] = useState([]);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const booksResponse = await bookApi.getBooks(user);
        const usersResponse = await bookApi.getUsers(user);
        const purchaseResponse = await bookApi.getPurchaseRequests(user);

        setBooks(booksResponse.data);
        setUsers(usersResponse.data);
        setPurchaseRequests(purchaseResponse.data);
      } catch (error) {
        handleLogError(error);
      } finally {
        setIsLoading(false);
      }
    };

    fetchData()
  }, []);

  if (!isAdmin) {
    return <Navigate to='/' />;
  }

  const processBookGenres = () => {
    const genreCounts = books.reduce((acc, book) => {
      acc[book.genre] = (acc[book.genre] || 0) + 1;
      return acc;
    }, {});

    return {
      labels: Object.keys(genreCounts),
      datasets: [{
        label: 'Books by Genre',
        data: Object.values(genreCounts),
        backgroundColor: [
          '#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0', '#9966FF',
          '#FF9F40', '#E7E9ED', '#8C564B', '#2CA02C', '#D62728'
        ]
      }]
    };
  };

  const processUserRoles = () => {
    const roleCounts = users.reduce((acc, user) => {
      acc[user.role] = (acc[user.role] || 0) + 1;
      return acc;
    }, {});

    return {
      labels: Object.keys(roleCounts),
      datasets: [{
        label: 'Users by Role',
        data: Object.values(roleCounts),
        backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56']
      }]
    };
  };

  const processPurchaseStatuses = () => {
    const statusCounts = purchaseRequests.reduce((acc, pr) => {
      acc[pr.status] = (acc[pr.status] || 0) + 1;
      return acc;
    }, {});

    const statuses = Object.keys(statusCounts);

    return {
      labels: ['Purchase Requests'],
      datasets: statuses.map((status, index) => ({
        label: status,
        data: [statusCounts[status]],
        backgroundColor: ['#4CAF50', '#F44336', '#FFC107'][index % 3],
      }))
    };
  };

  const panes = [
    {
      menuItem: { key: 'books', icon: 'book', content: 'Books' },
      render: () => (
        <Tab.Pane loading={isLoading}>
          <div style={{ maxWidth: '600px', margin: '0 auto' }}>
            <h3>Book Genres Distribution</h3>
            <Pie data={processBookGenres()} />
          </div>
        </Tab.Pane>
      )
    },
    {
      menuItem: { key: 'users', icon: 'users', content: 'Users' },
      render: () => (
        <Tab.Pane loading={isLoading}>
          <div style={{ maxWidth: '600px', margin: '0 auto' }}>
            <h3>User Roles Distribution</h3>
            <Pie data={processUserRoles()} />
          </div>
        </Tab.Pane>
      )
    },
    {
      menuItem: { key: 'purchases', icon: 'shopping cart', content: 'Purchases' },
      render: () => (
        <Tab.Pane loading={isLoading}>
          <div style={{ maxWidth: '800px', margin: '0 auto' }}>
            <h3>Purchase Request Statuses</h3>
            <Bar
              data={processPurchaseStatuses()}
              options={{
                responsive: true,
                scales: {
                  y: {
                    beginAtZero: true,
                    ticks: {
                      stepSize: 1
                    }
                  }
                }
              }}
            />
          </div>
        </Tab.Pane>
      )
    }
  ];

  return (
    <Container style={{ padding: '2em 0' }}>
      <h2>Analytics Dashboard</h2>
      <Tab panes={panes} menu={{ secondary: true, pointing: true }} />
    </Container>
  );
}

export default ChartsPage;