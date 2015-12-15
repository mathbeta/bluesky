package com.mathbeta.storage.message;

public interface IEndpoint {
	int read(byte[] b);
	void write(byte[] b, int off, int len);
	void close();
}
