package vn.edu.ueh.baoho.ssharing.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import vn.edu.ueh.baoho.ssharing.data.local.entity.NguoiDungEntity;
import vn.edu.ueh.baoho.ssharing.repository.UserRepository;

public class UserViewModel extends AndroidViewModel {
    private final UserRepository repository;

    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
    }

    public LiveData<NguoiDungEntity> getUserById(String userId) {
        return repository.getUserById(userId);
    }

    public void insertUser(NguoiDungEntity user) {
        repository.insertUser(user);
    }
}