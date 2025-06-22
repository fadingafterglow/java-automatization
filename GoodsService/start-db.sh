docker run \
  --name goods-db \
  --rm \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=root \
  -e POSTGRES_DB=goods \
  -p 5432:5432 \
  -v goods_db_volume:/var/lib/postgresql/data \
  postgres:17.2