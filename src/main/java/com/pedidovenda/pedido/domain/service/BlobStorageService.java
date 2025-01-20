package com.pedidovenda.pedido.domain.service;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import com.azure.storage.blob.*;
import com.azure.storage.blob.models.ParallelTransferOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.azure.storage.blob.models.BlobItem;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.azure.storage.blob.BlobContainerAsyncClient;
import com.azure.storage.blob.models.BlobStorageException;

@Service
public class BlobStorageService {

    private final BlobServiceAsyncClient blobServiceAsyncClient;
    private final String containerName;

    @Autowired
    public BlobStorageService(BlobServiceAsyncClient blobServiceAsyncClient, String containerName) {
        this.blobServiceAsyncClient = blobServiceAsyncClient;
        this.containerName = containerName;
    }

    private BlobContainerAsyncClient getContainerClient(String cn) {
        return blobServiceAsyncClient.getBlobContainerAsyncClient(containerName);
    }

    public Mono<String> uploadFile(MultipartFile file) throws IOException {
        return uploadFile(file, containerName);
    }

    public Mono<String> uploadFile(MultipartFile file, String containerName) throws IOException {
        ReadableByteChannel channel = Channels.newChannel(file.getInputStream());

        Flux<ByteBuffer> fileContentFlux = Flux.generate(
                sink -> {
                    ByteBuffer buffer = ByteBuffer.allocate(4096);
                    try {
                        int bytesRead = channel.read(buffer);
                        if (bytesRead == -1) {
                            sink.complete();
                        } else {
                            buffer.flip();
                            sink.next(buffer);
                            buffer.clear();
                        }
                    } catch (IOException e) {
                        sink.error(e);
                    }
                });

        return getContainerClient(containerName)
                .getBlobAsyncClient(file.getOriginalFilename())
                .upload(fileContentFlux, new ParallelTransferOptions()
                        .setBlockSizeLong(4L * 1024L * 1024L)
                        .setMaxConcurrency(5), true)
                .then(Mono.just("File uploaded successfully."));
    }

    public Flux<ByteBuffer> downloadFile(String containerName, String blobName) {
        BlobAsyncClient blobAsyncClient = getContainerClient(containerName).getBlobAsyncClient(blobName);
        return blobAsyncClient.downloadStream();
    }

    public Mono<Void> deleteFile(String containerName, String blobName) {
        return getContainerClient(containerName)
                .getBlobAsyncClient(blobName)
                .delete()
                .onErrorResume(BlobStorageException.class, e -> Mono.empty());
    }

    public Flux<BlobItem> listBlobs(String containerName) {
        return getContainerClient(containerName)
                .listBlobs();
    }
}