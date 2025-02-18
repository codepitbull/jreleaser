/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2020-2022 The JReleaser authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jreleaser.model;

import org.jreleaser.util.Env;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.jreleaser.util.CollectionUtils.newList;
import static org.jreleaser.util.Constants.HIDE;
import static org.jreleaser.util.Constants.UNSET;
import static org.jreleaser.util.StringUtils.isBlank;
import static org.jreleaser.util.StringUtils.isNotBlank;
import static org.jreleaser.util.Templates.resolveTemplate;

/**
 * @author Andres Almiray
 * @since 1.1.0
 */
public abstract class AbstractSshUploader extends AbstractUploader implements SshUploader {
    private String username;
    private String password;
    private String host;
    private Integer port;
    private String knownHostsFile;
    private String publicKey;
    private String privateKey;
    private String passphrase;
    private String fingerprint;
    private String path;
    private String downloadUrl;

    public AbstractSshUploader(String type) {
        super(type);
    }

    void setAll(AbstractSshUploader uploader) {
        this.username = uploader.username;
        this.password = uploader.password;
        this.host = uploader.host;
        this.port = uploader.port;
        this.knownHostsFile = uploader.knownHostsFile;
        this.publicKey = uploader.publicKey;
        this.privateKey = uploader.privateKey;
        this.passphrase = uploader.passphrase;
        this.fingerprint = uploader.fingerprint;
        this.path = uploader.path;
        this.downloadUrl = uploader.downloadUrl;
    }

    protected abstract String getEnvPrefix();

    @Override
    public String getResolvedUsername() {
        return Env.env(newList(
                getEnvPrefix() + "_" + Env.toVar(name) + "_USERNAME",
                "SSH_" + Env.toVar(name) + "_USERNAME",
                getEnvPrefix() + "_USERNAME",
                "SSH_USERNAME"),
            username);
    }

    @Override
    public String getResolvedPassword() {
        return Env.env(newList(
                getEnvPrefix() + "_" + Env.toVar(name) + "_PASSWORD",
                "SSH_" + Env.toVar(name) + "_PASSWORD",
                getEnvPrefix() + "_PASSWORD",
                "SSH_PASSWORD"),
            password);
    }

    @Override
    public String getResolvedHost() {
        return Env.env(newList(
                getEnvPrefix() + "_" + Env.toVar(name) + "_HOST",
                "SSH_" + Env.toVar(name) + "_HOST",
                getEnvPrefix() + "_HOST",
                "SSH_HOST"),
            host);
    }

    @Override
    public Integer getResolvedPort() {
        String value = Env.env(newList(
                getEnvPrefix() + "_" + Env.toVar(name) + "_PORT",
                "SSH_" + Env.toVar(name) + "_PORT",
                getEnvPrefix() + "_PORT",
                "SSH_PORT"),
            null == port ? "" : String.valueOf(port));
        return isBlank(value) ? 22 : Integer.parseInt(value);
    }

    @Override
    public String getResolvedPublicKey() {
        return Env.env(newList(
                getEnvPrefix() + "_" + Env.toVar(name) + "_PUBLIC_KEY",
                "SSH_" + Env.toVar(name) + "_PUBLIC_KEY",
                getEnvPrefix() + "_PUBLIC_KEY",
                "SSH_PUBLIC_KEY"),
            publicKey);
    }

    @Override
    public String getResolvedPrivateKey() {
        return Env.env(newList(
                getEnvPrefix() + "_" + Env.toVar(name) + "_PRIVATE_KEY",
                "SSH_" + Env.toVar(name) + "_PRIVATE_KEY",
                getEnvPrefix() + "_PRIVATE_KEY",
                "SSH_PRIVATE_KEY"),
            privateKey);
    }

    @Override
    public String getResolvedPassphrase() {
        return Env.env(newList(
                getEnvPrefix() + "_" + Env.toVar(name) + "_PASSPHRASE",
                "SSH_" + Env.toVar(name) + "_PASSPHRASE",
                getEnvPrefix() + "_PASSPHRASE",
                "SSH_PASSPHRASE"),
            passphrase);
    }

    @Override
    public String getResolvedFingerprint() {
        return Env.env(newList(
                getEnvPrefix() + "_" + Env.toVar(name) + "_FINGERPRINT",
                "SSH_" + Env.toVar(name) + "_FINGERPRINT",
                getEnvPrefix() + "_FINGERPRINT",
                "SSH_FINGERPRINT"),
            fingerprint);
    }

    @Override
    public String getResolvedPath(JReleaserContext context, Artifact artifact) {
        Map<String, Object> p = artifactProps(context.fullProps(), artifact);
        p.putAll(getResolvedExtraProperties());
        return resolveTemplate(path, p);
    }

    @Override
    public String getResolvedDownloadUrl(JReleaserContext context, Artifact artifact) {
        return getResolvedDownloadUrl(context.fullProps(), artifact);
    }

    @Override
    public String getResolvedDownloadUrl(Map<String, Object> props, Artifact artifact) {
        Map<String, Object> p = new LinkedHashMap<>(artifactProps(props, artifact));
        p.putAll(getResolvedExtraProperties());
        return resolveTemplate(downloadUrl, p);
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String getDownloadUrl() {
        return downloadUrl;
    }

    @Override
    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public Integer getPort() {
        return port;
    }

    @Override
    public void setPort(Integer port) {
        this.port = port;
    }

    @Override
    public String getKnownHostsFile() {
        return knownHostsFile;
    }

    @Override
    public void setKnownHostsFile(String knownHostsFile) {
        this.knownHostsFile = knownHostsFile;
    }

    @Override
    public String getPublicKey() {
        return publicKey;
    }

    @Override
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public String getPrivateKey() {
        return privateKey;
    }

    @Override
    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    @Override
    public String getPassphrase() {
        return passphrase;
    }

    @Override
    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }

    @Override
    public String getFingerprint() {
        return fingerprint;
    }

    @Override
    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    @Override
    protected void asMap(Map<String, Object> props, boolean full) {
        props.put("host", isNotBlank(getResolvedHost()) ? HIDE : UNSET);
        props.put("port", getResolvedPort());
        props.put("username", isNotBlank(getResolvedUsername()) ? HIDE : UNSET);
        props.put("password", isNotBlank(getResolvedPassword()) ? HIDE : UNSET);
        props.put("knownHostsFile", knownHostsFile);
        props.put("publicKey", isNotBlank(getResolvedPublicKey()) ? HIDE : UNSET);
        props.put("privateKey", isNotBlank(getResolvedPrivateKey()) ? HIDE : UNSET);
        props.put("passphrase", isNotBlank(getResolvedPassphrase()) ? HIDE : UNSET);
        props.put("fingerprint", isNotBlank(getResolvedFingerprint()) ? HIDE : UNSET);
        props.put("path", path);
        props.put("downloadUrl", downloadUrl);
    }
}
