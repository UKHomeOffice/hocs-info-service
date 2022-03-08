FROM quay.io/ukhomeofficedigital/hocs-base-image-build as builder

COPY build/libs/hocs-info-service.jar ./
COPY scripts/run.sh ./

COPY . .
RUN ./gradlew clean assemble --no-daemon

RUN java -Djarmode=layertools -jar ./build/libs/hocs-info-service.jar extract

FROM quay.io/ukhomeofficedigital/hocs-base-image

COPY --from=builder --chown=user_hocs:group_hocs ./scripts/run.sh ./
COPY --from=builder --chown=user_hocs:group_hocs ./spring-boot-loader/ ./
COPY --from=builder --chown=user_hocs:group_hocs ./dependencies/ ./
COPY --from=builder --chown=user_hocs:group_hocs ./application/ ./

CMD ["sh", "/app/run.sh"]
