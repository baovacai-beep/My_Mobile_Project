package vn.edu.ueh.baoho.ssharing.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import vn.edu.ueh.baoho.ssharing.data.local.entity.VoucherEntity;
import vn.edu.ueh.baoho.ssharing.model.Voucher;
import vn.edu.ueh.baoho.ssharing.repository.VoucherRepository;

public class VoucherViewModel extends AndroidViewModel {
    private final VoucherRepository repository;
    private final LiveData<List<VoucherEntity>> allVouchers;

    // LiveData lưu trữ trạng thái tab bộ lọc, sống sót khi xoay màn hình
    private final MutableLiveData<String> selectedFilter = new MutableLiveData<>("ALL");

    public VoucherViewModel(@NonNull Application application) {
        super(application);
        repository = new VoucherRepository(application);
        allVouchers = repository.getAllVouchers();
    }

    public LiveData<List<VoucherEntity>> getAllVouchers() {
        return allVouchers;
    }

    public LiveData<String> getSelectedFilter() {
        return selectedFilter;
    }

    public void setFilter(String filter) {
        selectedFilter.setValue(filter);
    }

    public void themVoucher(Voucher voucher) {
        repository.insertDomainVoucher(voucher);
    }
}