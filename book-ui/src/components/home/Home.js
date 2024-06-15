import React, { useState, useEffect } from 'react';
import { Container, Segment, Dimmer, Loader, Button } from 'semantic-ui-react';
import { useNavigate  } from 'react-router-dom';
import './Home.css';

const quotes = [
  "A room without books is like a body without a soul. – Marcus Tullius Cicero",
  "The only thing you absolutely have to know, is the location of the library. – Albert Einstein",
  "So many books, so little time. – Frank Zappa",
  "A book is a dream that you hold in your hand. – Neil Gaiman",
  "Reading is to the mind what exercise is to the body. – Joseph Addison"
];

function Home() {
  const [isLoading, setIsLoading] = useState(false);
  const [quoteIndex, setQuoteIndex] = useState(0);
  const navigate = useNavigate ();

  useEffect(() => {
    const quoteInterval = setInterval(() => {
      setQuoteIndex((prevIndex) => (prevIndex + 1) % quotes.length);
    }, 5000);

    return () => clearInterval(quoteInterval);
  }, []);

  if (isLoading) {
    return (
        <Segment basic className='loading-segment'>
            <Dimmer active inverted>
                <Loader inverted size='huge'>Loading</Loader>
            </Dimmer>
        </Segment>
    );
  }

  return (
      <div className='home-background'>
          <Container text className='home-content'>
            <Segment>
              <p style={{ fontStyle: 'italic', fontSize: '1.2em', textAlign: 'center' }}>
                {quotes[quoteIndex]}
              </p>
            </Segment>

            <div style={{ textAlign: 'center', marginTop: '2em' }}>
              <Button primary onClick={() => navigate('/library')}>Go to Library</Button>
            </div>
          </Container>
      </div>
  );
}

export default Home;
