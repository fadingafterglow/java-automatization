openapi: 3.0.3
info:
  title: [:main-h:] Service
  description: [:main-h:] Service
  version: '1.0'

tags:
  - name: [:main-l-sh:]-controller
    description: [:main-h:] Service Controller

servers:
  - url: https://smart.ukma.edu.ua

paths:
  /api/[:main-l-sh:]:
    get:
      tags:
        - [:main-l-sh:]-controller
      summary: Get list of [:plural-l:]
      operationId: getListOf[:plural-h-ns:]
      parameters:
        - in: query
          required: false
          name: restrict
          schema:
            type: string
      responses:
        200:
          description: List of [:plural-l:]
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/[:main-h-ns:]ListResponse'

    put:
      tags:
        - [:main-l-sh:]-controller
      summary: Create [:main-l:]
      operationId: create[:main-h-ns:]
      requestBody:
        required: true
        content:
          application/json:
            schema:
             $ref: '#/components/schemas/[:main-h-ns:]View'
      responses:
        200:
          description: Id of new [:main-l:]
          content:
            application/json:
              schema:
                type: integer

  /api/[:main-l-sh:]/{id}:
    parameters:
      - in: path
        required: true
        name: id
        schema:
          type: integer

    get:
      tags:
        - [:main-l-sh:]-controller
      summary: Get [:main-l:] by its id
      operationId: get[:main-h-ns:]ById
      responses:
        200:
          description: [:main-s:] with corresponding id
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/[:main-h-ns:]Response'

    post:
      tags:
        - [:main-l-sh:]-controller
      summary: Update [:main-l:]
      operationId: update[:main-h-ns:]
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/[:main-h-ns:]View'
      responses:
        200:
          description: Boolean indicating whether [:main-l:] was updated
          content:
            application/json:
              schema:
                type: boolean

    delete:
      tags:
        - [:main-l-sh:]-controller
      summary: Delete [:main-l:] by id
      operationId: delete[:main-h-ns:]ById
      responses:
        200:
          description: Boolean indicating whether [:main-l:] was deleted
          content:
            application/json:
              schema:
                type: boolean

  /api/[:main-l-sh:]/user/{email}:
    get:
      tags:
        - [:main-l-sh:]-controller
      summary: Get list of [:plural-l:] by user email
      operationId: getListOf[:plural-h-ns:]ByUserEmail
      parameters:
        - in: path
          required: true
          name: email
          schema:
            type: string
      responses:
        200:
          description: List of [:plural-l:] with corresponding user email
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/[:main-h-ns:]ListResponse'
  
components:
  schemas:
    [:main-h-ns:]View:
      type: object
      properties:

      required:
        -

    [:main-h-ns:]Response:
      type: object
      properties:
        id:
          type: integer

      required:
        - id
        -

    [:main-h-ns:]ListResponse:
      type: object
      properties:
        total:
          type: integer
          format: int64
        items:
          type: array
          items:
            $ref: '#/components/schemas/[:main-h-ns:]Response'
      required:
        - total
        - items