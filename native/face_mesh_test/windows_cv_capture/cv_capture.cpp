#pragma once

#include "cv_capture.h"

namespace cvcap {
    int FrameGrabber::listDevices(std::vector<std::string>& list)
    {
        //COM Library Initialization

        ICreateDevEnum* pDevEnum = nullptr;
        IEnumMoniker* pEnum = nullptr;
        int deviceCounter = 0;
        CoInitialize(NULL);

        HRESULT hr = CoCreateInstance(CLSID_SystemDeviceEnum, NULL,
            CLSCTX_INPROC_SERVER, IID_ICreateDevEnum,
            reinterpret_cast<void**>(&pDevEnum));


        if (SUCCEEDED(hr))
        {
            // Create an enumerator for the video capture category.
            hr = pDevEnum->CreateClassEnumerator(
                CLSID_VideoInputDeviceCategory,
                &pEnum, 0);

            if (hr == S_OK) {

                printf("SETUP: Looking For Capture Devices\n");
                IMoniker* pMoniker = nullptr;

                while (pEnum->Next(1, &pMoniker, nullptr) == S_OK) {

                    IPropertyBag* pPropBag;
                    hr = pMoniker->BindToStorage(nullptr, nullptr, IID_IPropertyBag,
                        (void**)(&pPropBag));

                    if (FAILED(hr)) {
                        pMoniker->Release();
                        continue;  // Skip this one, maybe the next one will work.
                    }


                    // Find the description or friendly name.
                    VARIANT varName;
                    VariantInit(&varName);
                    hr = pPropBag->Read(L"Description", &varName, nullptr);

                    if (FAILED(hr)) hr = pPropBag->Read(L"FriendlyName", &varName, nullptr);

                    if (SUCCEEDED(hr))
                    {

                        hr = pPropBag->Read(L"FriendlyName", &varName, nullptr);

                        int count = 0;
                        char tmp[255] = { 0 };
                        while (varName.bstrVal[count] != 0x00 && count < 255)
                        {
                            tmp[count] = (char)varName.bstrVal[count];
                            count++;
                        }
                        list.push_back(tmp);

                    }

                    pPropBag->Release();
                    pPropBag = nullptr;

                    pMoniker->Release();
                    pMoniker = nullptr;

                    deviceCounter++;
                }

                pDevEnum->Release();
                pDevEnum = nullptr;

                pEnum->Release();
                pEnum = nullptr;
            }
        }

        return deviceCounter;
    }

    bool FrameGrabber::open(int idx) {
        if (!cap || cap->isOpened()) return false;
        cap->open(idx, cv::CAP_DSHOW);
        opened = cap->isOpened();
        return opened;
    }

    int FrameGrabber::readFrame(cv::Mat& frame) {
        if (cap) {
            bool success = cap->read(frame);

            if (frame.empty()) {
                return 1;
            }

            if (success) {
                return 0;
            }
        }

        return 2;
    }

    bool FrameGrabber::close() {

        if (cap) {
            cap->release();
            opened = false;
            return true;
        }
        opened = false;
        return false;
    }

    bool FrameGrabber::set(int k, double v) {
        if (cap) {
            return cap->set(k, v);
        }
        return false;
    }

    double FrameGrabber::get(int k) {
        if (cap) {
            return cap->get(k);
        }
        return -1.0;
    }
}


