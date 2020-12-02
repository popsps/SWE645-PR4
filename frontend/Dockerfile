# alpine version of node image
FROM node:alpine AS angular_builder
# change destination working directory to /user/src/app
WORKDIR /user/src/app
ADD package.json ./
RUN npm install
RUN npm install -g @angular/cli
COPY . .
RUN npm run build
#EXPOSE 4200
#CMD ng serve --host 0.0.0.0

FROM nginx:alpine
COPY default.conf /etc/nginx/conf.d/default.conf
COPY --from=angular_builder /user/src/app/dist/frontend /usr/share/nginx/html
EXPOSE 80
