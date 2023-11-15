FROM node:14 as builder
WORKDIR /usr/src/app
COPY package*.json ./
RUN npm i
COPY . ./
RUN npm run build

FROM bitnami/nginx:latest
COPY --from=builder /usr/src/app/build /app
COPY nginx.conf /opt/bitnami/nginx/conf/server_blocks/
EXPOSE 8080
CMD ["nginx", "-g", "daemon off;"]
