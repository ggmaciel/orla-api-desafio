FROM eclipse-temurin:21-alpine

RUN addgroup --system --gid 1000 orla \
    && adduser --system --uid 1000 --ingroup orla --home /opt orla-api \
    && chown -R orla-api:orla /opt

COPY target/*.jar /opt/orla-api.jar
COPY src/docker/entrypoint.sh /entrypoint.sh

RUN chmod +x /entrypoint.sh && chown orla-api:orla /opt/orla-api.jar /entrypoint.sh

USER orla-api

ENTRYPOINT ["/entrypoint.sh"]