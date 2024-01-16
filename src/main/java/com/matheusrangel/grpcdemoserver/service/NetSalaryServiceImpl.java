package com.matheusrangel.grpcdemoserver.service;

import com.matheusrangel.grpcdemo.service.NetSalaryGrpc;
import com.matheusrangel.grpcdemo.service.NetSalaryRequest;
import com.matheusrangel.grpcdemo.service.NetSalaryResponse;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class NetSalaryServiceImpl extends NetSalaryGrpc.NetSalaryImplBase {

    @Override
    public void calculate(NetSalaryRequest request, StreamObserver<NetSalaryResponse> responseObserver) {
        double grossSalary = request.getGrossSalary();

        // Calculate Contributions
        double inss = calculateINSS(grossSalary);
        double irrf = calculateIRRF(grossSalary, inss);

        double netSalary = grossSalary - inss - irrf;

        // Build and send the response
        NetSalaryResponse response = NetSalaryResponse.newBuilder()
                .setNetSalary(netSalary)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private double calculateINSS(double grossSalary) {
        double inss = 0;

        if (grossSalary <= 1320.00) {
            inss = grossSalary * 0.075;
        } else if (grossSalary <= 2571.29) {
            inss = grossSalary * 0.09;
        } else if (grossSalary <= 3856.94) {
            inss = grossSalary * 0.12;
        } else if (grossSalary <= 7507.49) {
            inss = grossSalary * 0.14;
        } else {
            inss = 877.24;
        }

        return inss;
    }

    private double calculateIRRF(double grossSalary, double inss) {
        double irrf = 0;
        double calcBasis = grossSalary - inss;

        if (calcBasis <= 2112.00) {
            irrf = 0;
        } else if (calcBasis <= 2826.65) {
            irrf = calcBasis * 0.075 - 158.40;
        } else if (calcBasis <= 3751.05) {
            irrf = calcBasis * 0.15 - 370.40;
        } else if (calcBasis <= 4664.68) {
            irrf = calcBasis * 0.225 - 651.73;
        } else {
            irrf = calcBasis * 0.275 - 884.96;
        }

        return irrf;
    }
}

