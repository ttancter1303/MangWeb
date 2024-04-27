package org.example;

import org.example.action.FolderProvider;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class ImageCrawler {
    private FolderProvider folderProvider;
    private String mangaTitle;
    private String chapterNumber;

    public ImageCrawler(FolderProvider folderProvider, String mangaTitle, String chapterNumber) {
        this.folderProvider = folderProvider;
        this.mangaTitle = mangaTitle;
        this.chapterNumber = chapterNumber;
    }

    private static final String USER_AGENT = "Mozilla/5.0 (compatible; YandexAccessibilityBot/3.0; +http://yandex.com/bots)";

    public void crawlImages(String url) {
        try {
            Set<String> imageUrls = new HashSet<>();
            Document document = Jsoup.connect(url).userAgent(USER_AGENT).get();
            Elements imgElements = document.select("img#img");

            for (Element imgElement : imgElements) {
                String imageUrl = imgElement.absUrl("src");
                if (!imageUrl.isEmpty()) {
                    imageUrls.add(imageUrl);
                }
            }

            for (String imageUrl : imageUrls) {
                downloadImage(imageUrl);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getImageNameFromUrl(String imageUrl) {
        String[] parts = imageUrl.split("/");
        String imageName = parts[parts.length - 1];
        return imageName.substring(0, imageName.lastIndexOf('.'));
    }

    public void downloadImage(String imageUrl) {
        try {
            if (folderProvider == null) {
                System.err.println("Error: folderProvider is null");
                return;
            }

            // Sử dụng folderProvider.getFolderPath() để truy cập đường dẫn folder
            String folderPath = folderProvider.getFolderPath(mangaTitle, chapterNumber);
            System.out.println("Downloading images from: " + folderPath);

            HttpURLConnection connection = (HttpURLConnection) new URL(imageUrl).openConnection();
            connection.setRequestProperty("User-Agent", USER_AGENT);

            // Kiểm tra xem connection có thành công không
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (InputStream inputStream = connection.getInputStream()) {
                    // Tạo đường dẫn cho tệp ảnh cần lưu.
                    String fileName = Paths.get(imageUrl).getFileName().toString();
                    String destinationPath = Paths.get(folderPath, fileName).toString();

                    try (OutputStream outputStream = new FileOutputStream(destinationPath)) {
                        byte[] buffer = new byte[2048];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                        System.out.println("Downloading " + fileName);
                    }
                }
            } else {
                System.err.println("Failed to download image. Response Code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void imageDownloader2(String imageUrl) {
        try {
            if (folderProvider == null) {
                System.err.println("Error: folderProvider is null");
                return;
            }

            // Sử dụng folderProvider.getFolderPath() để truy cập đường dẫn folder
            String folderPath = folderProvider.getFolderPath(mangaTitle, chapterNumber);
            System.out.println("Downloading images from: " + folderPath);

            String imageName = Paths.get(imageUrl).getFileName().toString();
            imageName = imageName.replaceAll("[^a-zA-Z0-9.-]", "");
            imageName = imageName.split("\\.")[0];

            URL url = new URL(imageUrl);
            try (InputStream inputStream = url.openStream()) {
                // Tạo đường dẫn cho tệp ảnh cần lưu.
                String destinationPath = Paths.get(folderPath, imageName + ".jpg").toString();
                // Mở tệp đích để lưu ảnh.
                try (OutputStream outputStream = new FileOutputStream(destinationPath)) {
                    byte[] buffer = new byte[2048];
                    int length;
                    while ((length = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, length);
                    }
                    // Đóng luồng đầu vào và đầu ra.
                    System.out.println("Tải ảnh thành công và lưu tại: " + destinationPath);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
