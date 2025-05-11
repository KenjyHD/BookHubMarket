FROM node:18-alpine

WORKDIR /app

COPY . .

RUN npm ci

RUN npm run build
RUN npm install -g serve

EXPOSE 3000
CMD ["serve", "-s", "build"]