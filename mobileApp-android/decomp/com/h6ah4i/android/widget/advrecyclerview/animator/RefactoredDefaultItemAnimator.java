package com.h6ah4i.android.widget.advrecyclerview.animator;

import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import com.h6ah4i.android.widget.advrecyclerview.animator.impl.AddAnimationInfo;
import com.h6ah4i.android.widget.advrecyclerview.animator.impl.ChangeAnimationInfo;
import com.h6ah4i.android.widget.advrecyclerview.animator.impl.ItemAddAnimationManager;
import com.h6ah4i.android.widget.advrecyclerview.animator.impl.ItemAnimationInfo;
import com.h6ah4i.android.widget.advrecyclerview.animator.impl.ItemChangeAnimationManager;
import com.h6ah4i.android.widget.advrecyclerview.animator.impl.ItemMoveAnimationManager;
import com.h6ah4i.android.widget.advrecyclerview.animator.impl.ItemRemoveAnimationManager;
import com.h6ah4i.android.widget.advrecyclerview.animator.impl.MoveAnimationInfo;
import com.h6ah4i.android.widget.advrecyclerview.animator.impl.RemoveAnimationInfo;

public class RefactoredDefaultItemAnimator extends GeneralItemAnimator {

    protected static class DefaultItemAddAnimationManager extends ItemAddAnimationManager {
        public DefaultItemAddAnimationManager(BaseItemAnimator baseItemAnimator) {
            super(baseItemAnimator);
        }

        public boolean addPendingAnimation(ViewHolder viewHolder) {
            endAnimation(viewHolder);
            ViewCompat.setAlpha(viewHolder.itemView, 0.0f);
            enqueuePendingAnimationInfo(new AddAnimationInfo(viewHolder));
            return true;
        }

        protected void onAnimationCancel(AddAnimationInfo addAnimationInfo, ViewHolder viewHolder) {
            ViewCompat.setAlpha(viewHolder.itemView, 1.0f);
        }

        protected void onAnimationEndedBeforeStarted(AddAnimationInfo addAnimationInfo, ViewHolder viewHolder) {
            ViewCompat.setAlpha(viewHolder.itemView, 1.0f);
        }

        protected void onAnimationEndedSuccessfully(AddAnimationInfo addAnimationInfo, ViewHolder viewHolder) {
        }

        protected void onCreateAnimation(AddAnimationInfo addAnimationInfo) {
            ViewPropertyAnimatorCompat animate = ViewCompat.animate(addAnimationInfo.holder.itemView);
            animate.alpha(1.0f);
            animate.setDuration(getDuration());
            startActiveItemAnimation(addAnimationInfo, addAnimationInfo.holder, animate);
        }
    }

    protected static class DefaultItemChangeAnimationManager extends ItemChangeAnimationManager {
        public DefaultItemChangeAnimationManager(BaseItemAnimator baseItemAnimator) {
            super(baseItemAnimator);
        }

        public boolean addPendingAnimation(ViewHolder viewHolder, ViewHolder viewHolder2, int i, int i2, int i3, int i4) {
            float translationX = ViewCompat.getTranslationX(viewHolder.itemView);
            float translationY = ViewCompat.getTranslationY(viewHolder.itemView);
            float alpha = ViewCompat.getAlpha(viewHolder.itemView);
            endAnimation(viewHolder);
            int i5 = (int) (((float) (i3 - i)) - translationX);
            int i6 = (int) (((float) (i4 - i2)) - translationY);
            ViewCompat.setTranslationX(viewHolder.itemView, translationX);
            ViewCompat.setTranslationY(viewHolder.itemView, translationY);
            ViewCompat.setAlpha(viewHolder.itemView, alpha);
            if (viewHolder2 != null) {
                endAnimation(viewHolder2);
                ViewCompat.setTranslationX(viewHolder2.itemView, (float) (-i5));
                ViewCompat.setTranslationY(viewHolder2.itemView, (float) (-i6));
                ViewCompat.setAlpha(viewHolder2.itemView, 0.0f);
            }
            enqueuePendingAnimationInfo(new ChangeAnimationInfo(viewHolder, viewHolder2, i, i2, i3, i4));
            return true;
        }

        protected void onAnimationCancel(ChangeAnimationInfo changeAnimationInfo, ViewHolder viewHolder) {
        }

        protected void onAnimationEndedBeforeStarted(ChangeAnimationInfo changeAnimationInfo, ViewHolder viewHolder) {
            View view = viewHolder.itemView;
            ViewCompat.setAlpha(view, 1.0f);
            ViewCompat.setTranslationX(view, 0.0f);
            ViewCompat.setTranslationY(view, 0.0f);
        }

        protected void onAnimationEndedSuccessfully(ChangeAnimationInfo changeAnimationInfo, ViewHolder viewHolder) {
            View view = viewHolder.itemView;
            ViewCompat.setAlpha(view, 1.0f);
            ViewCompat.setTranslationX(view, 0.0f);
            ViewCompat.setTranslationY(view, 0.0f);
        }

        protected void onCreateChangeAnimationForNewItem(ChangeAnimationInfo changeAnimationInfo) {
            ViewPropertyAnimatorCompat animate = ViewCompat.animate(changeAnimationInfo.newHolder.itemView);
            animate.translationX(0.0f);
            animate.translationY(0.0f);
            animate.setDuration(getDuration());
            animate.alpha(1.0f);
            startActiveItemAnimation(changeAnimationInfo, changeAnimationInfo.newHolder, animate);
        }

        protected void onCreateChangeAnimationForOldItem(ChangeAnimationInfo changeAnimationInfo) {
            ViewPropertyAnimatorCompat animate = ViewCompat.animate(changeAnimationInfo.oldHolder.itemView);
            animate.setDuration(getDuration());
            animate.translationX((float) (changeAnimationInfo.toX - changeAnimationInfo.fromX));
            animate.translationY((float) (changeAnimationInfo.toY - changeAnimationInfo.fromY));
            animate.alpha(0.0f);
            startActiveItemAnimation(changeAnimationInfo, changeAnimationInfo.oldHolder, animate);
        }
    }

    protected static class DefaultItemMoveAnimationManager extends ItemMoveAnimationManager {
        public DefaultItemMoveAnimationManager(BaseItemAnimator baseItemAnimator) {
            super(baseItemAnimator);
        }

        public boolean addPendingAnimation(ViewHolder viewHolder, int i, int i2, int i3, int i4) {
            View view = viewHolder.itemView;
            int translationX = (int) (((float) i) + ViewCompat.getTranslationX(viewHolder.itemView));
            int translationY = (int) (((float) i2) + ViewCompat.getTranslationY(viewHolder.itemView));
            endAnimation(viewHolder);
            int i5 = i3 - translationX;
            int i6 = i4 - translationY;
            ItemAnimationInfo moveAnimationInfo = new MoveAnimationInfo(viewHolder, translationX, translationY, i3, i4);
            if (i5 == 0 && i6 == 0) {
                dispatchFinished((MoveAnimationInfo) moveAnimationInfo, moveAnimationInfo.holder);
                moveAnimationInfo.clear(moveAnimationInfo.holder);
                return false;
            }
            if (i5 != 0) {
                ViewCompat.setTranslationX(view, (float) (-i5));
            }
            if (i6 != 0) {
                ViewCompat.setTranslationY(view, (float) (-i6));
            }
            enqueuePendingAnimationInfo(moveAnimationInfo);
            return true;
        }

        protected void onAnimationCancel(MoveAnimationInfo moveAnimationInfo, ViewHolder viewHolder) {
            View view = viewHolder.itemView;
            int i = moveAnimationInfo.toX - moveAnimationInfo.fromX;
            int i2 = moveAnimationInfo.toY - moveAnimationInfo.fromY;
            if (i != 0) {
                ViewCompat.animate(view).translationX(0.0f);
            }
            if (i2 != 0) {
                ViewCompat.animate(view).translationY(0.0f);
            }
            if (i != 0) {
                ViewCompat.setTranslationX(view, 0.0f);
            }
            if (i2 != 0) {
                ViewCompat.setTranslationY(view, 0.0f);
            }
        }

        protected void onAnimationEndedBeforeStarted(MoveAnimationInfo moveAnimationInfo, ViewHolder viewHolder) {
            View view = viewHolder.itemView;
            ViewCompat.setTranslationY(view, 0.0f);
            ViewCompat.setTranslationX(view, 0.0f);
        }

        protected void onAnimationEndedSuccessfully(MoveAnimationInfo moveAnimationInfo, ViewHolder viewHolder) {
        }

        protected void onCreateAnimation(MoveAnimationInfo moveAnimationInfo) {
            View view = moveAnimationInfo.holder.itemView;
            int i = moveAnimationInfo.toY - moveAnimationInfo.fromY;
            if (moveAnimationInfo.toX - moveAnimationInfo.fromX != 0) {
                ViewCompat.animate(view).translationX(0.0f);
            }
            if (i != 0) {
                ViewCompat.animate(view).translationY(0.0f);
            }
            ViewPropertyAnimatorCompat animate = ViewCompat.animate(view);
            animate.setDuration(getDuration());
            startActiveItemAnimation(moveAnimationInfo, moveAnimationInfo.holder, animate);
        }
    }

    protected static class DefaultItemRemoveAnimationManager extends ItemRemoveAnimationManager {
        public DefaultItemRemoveAnimationManager(BaseItemAnimator baseItemAnimator) {
            super(baseItemAnimator);
        }

        public boolean addPendingAnimation(ViewHolder viewHolder) {
            endAnimation(viewHolder);
            enqueuePendingAnimationInfo(new RemoveAnimationInfo(viewHolder));
            return true;
        }

        protected void onAnimationCancel(RemoveAnimationInfo removeAnimationInfo, ViewHolder viewHolder) {
        }

        protected void onAnimationEndedBeforeStarted(RemoveAnimationInfo removeAnimationInfo, ViewHolder viewHolder) {
            ViewCompat.setAlpha(viewHolder.itemView, 1.0f);
        }

        protected void onAnimationEndedSuccessfully(RemoveAnimationInfo removeAnimationInfo, ViewHolder viewHolder) {
            ViewCompat.setAlpha(viewHolder.itemView, 1.0f);
        }

        protected void onCreateAnimation(RemoveAnimationInfo removeAnimationInfo) {
            ViewPropertyAnimatorCompat animate = ViewCompat.animate(removeAnimationInfo.holder.itemView);
            animate.setDuration(getDuration());
            animate.alpha(0.0f);
            startActiveItemAnimation(removeAnimationInfo, removeAnimationInfo.holder, animate);
        }
    }

    protected void onSchedulePendingAnimations() {
        schedulePendingAnimationsByDefaultRule();
    }

    protected void onSetup() {
        setItemAddAnimationsManager(new DefaultItemAddAnimationManager(this));
        setItemRemoveAnimationManager(new DefaultItemRemoveAnimationManager(this));
        setItemChangeAnimationsManager(new DefaultItemChangeAnimationManager(this));
        setItemMoveAnimationsManager(new DefaultItemMoveAnimationManager(this));
    }
}
