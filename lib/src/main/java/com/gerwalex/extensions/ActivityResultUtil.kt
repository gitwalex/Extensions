package com.gerwalex.lib.main

import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
object ActivityForResultUtil {
    class ActivityResultUtil<Input, Result>(
        caller: ActivityResultCaller,
        contract: ActivityResultContract<Input, Result>,
    ) {

        private lateinit var onActivityResult: OnActivityResult<Result>
        private val launcher: ActivityResultLauncher<Input>

        init {
            launcher = caller.registerForActivityResult(contract) { result: Result -> callOnActivityResult(result) }
        }

        private fun callOnActivityResult(result: Result) {
            onActivityResult.onActivityResult(result)
        }

        /**
         * Executes an {@link ActivityResultContract}.
         *
         * <p>This method throws {@link android.content.ActivityNotFoundException}
         * if there was no Activity found to run the given Intent.

         * @param input the input required to execute an {@link ActivityResultContract}.
         *
         * @throws android.content.ActivityNotFoundException
         */
        fun launch(input: Input, onActivityResult: OnActivityResult<Result>) {
            this.onActivityResult = onActivityResult
            launcher.launch(input)
        }
    }

    /**
     * Regisriert ActivityForResult für StartActivityForResult
     *
     * @param onActivityResult Resulthandler
     */
    @JvmStatic
    fun ActivityResultCaller.registerActivityForResult():
            ActivityResultUtil<Intent, ActivityResult> {
        return registerForActivityResult(StartActivityForResult())
    }

    /**
     * Regisriert ActivityForResult
     *
     * @param contract der ActivityResultContract.
     *
     * @param onActivityResult Resulthandler
     */
    @JvmStatic
    fun <Input, Result> ActivityResultCaller.registerForActivityResult(
        contract: ActivityResultContract<Input, Result>,
    ): ActivityResultUtil<Input, Result> {
        return ActivityResultUtil(this, contract)
    }

    /**
     * Callback interface
     */
    fun interface OnActivityResult<O> {

        /**
         * Called after receiving a result from the target activity
         */
        fun onActivityResult(result: O)
    }
}